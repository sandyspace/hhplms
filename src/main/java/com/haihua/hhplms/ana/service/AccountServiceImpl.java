package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.Permission;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.mapper.AccountMapper;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.ana.entity.*;
import com.haihua.hhplms.ana.mapper.AccountMapper;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.ListUtils;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.security.auth.ajax.WebBasedAjaxAuthenticationService;
import com.haihua.hhplms.security.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("accountService")
public class AccountServiceImpl implements AccountService, WebBasedAjaxAuthenticationService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    @Qualifier("roleService")
    private RoleService roleService;

    @Autowired
    @Qualifier("permissionService")
    private PermissionService permissionService;

    @Autowired
    @Qualifier("accountRoleRelationshipService")
    private AccountRoleRelationshipService accountRoleRelationshipService;

    public PageWrapper<List<AccountVO>> loadAccountsByPage(String loginNameLike,
                                                           String realNameLike,
                                                           String mobileLike,
                                                           String gender,
                                                           String type,
                                                           String status,
                                                           Long companyInfoSid,
                                                           Integer pageNo,
                                                           Integer pageSize) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看");
        }
        Map<String, Object> params = new HashMap<>();
        if (WebUtils.isEmployee()) {
            if (!StringUtils.isBlank(type)) {
                params.put("type", type);
            }
            if (Objects.nonNull(companyInfoSid)) {
                params.put("companyInfoSid", companyInfoSid);
            }
        } else {
            params.put("type", Account.Type.COMPANY.getCode());
            params.put("companyInfoSid", WebUtils.getCompanyId());
        }
        if (!StringUtils.isBlank(loginNameLike)) {
            params.put("loginNameLike", loginNameLike);
        }
        if (!StringUtils.isBlank(realNameLike)) {
            params.put("realNameLike", realNameLike);
        }
        if (!StringUtils.isBlank(mobileLike)) {
            params.put("mobileLike", mobileLike);
        }
        if (!StringUtils.isBlank(gender)) {
            params.put("gender", gender);
        }
        if (!StringUtils.isBlank(status)) {
            params.put("status", status);
        }
        return loadAccountsByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<AccountVO>> loadAccountsByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<AccountVO>> pageOfAccounts = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageNo * pageSize);
            List<Account> matchedAccounts = findByParams(params);
            if (Objects.nonNull(matchedAccounts) && !matchedAccounts.isEmpty()) {
                pageOfAccounts.setResult(matchedAccounts.stream()
                        .map(matchedAccount -> new AccountVO(matchedAccount))
                        .collect(Collectors.toList()));
            }
        }
        return pageOfAccounts;
    }

    public AccountVO loadDetail(Long sid) {
        if (WebUtils.isMember()) {
            if (!WebUtils.getUserId().equals(sid)) {
                throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看其他人的账户详细信息");
            }
        }
        Account matchedAccount = findBySid(sid);
        if (Objects.isNull(matchedAccount)) {
            throw new ServiceException("ID为[" + sid + "]的账号不存在");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(matchedAccount.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限查看其他" + Account.Type.COMPANY.getName() + "的账户详情");
            }
        }
        AccountVO accountDetail = new AccountVO(matchedAccount);
        List<Role> matchedRoles = roleService.findRolesOfGivenAccount(sid);
        if (Objects.nonNull(matchedRoles) && !matchedRoles.isEmpty()) {
            List<Long> assignedRoleSids = new ArrayList<>(matchedRoles.size());
            List<RoleVO> assignedRoles = new ArrayList<>(matchedRoles.size());
            for (Role matchedRole : matchedRoles) {
                assignedRoleSids.add(matchedRole.getSid());
                assignedRoles.add(new RoleVO(matchedRole));
            }

            accountDetail.setGrantedRoles(assignedRoles);
            List<Permission> assignedApiList = permissionService.findPermissionsOfGivenRoles(assignedRoleSids, Permission.Type.API);
            if (Objects.nonNull(assignedApiList) && !assignedApiList.isEmpty()) {
                accountDetail.setGrantedApiList(assignedApiList.stream()
                        .map(assignedApi -> new PermissionVO(assignedApi))
                        .collect(Collectors.toList()));
            }

            List<Permission> assignedPermissions = permissionService.findPermissionsOfGivenRoles(assignedRoleSids, Permission.Type.PAGE);
            final Map<Long, PermissionVO> grantedPermissionMap = new HashMap<>();
            assignedPermissions.forEach(assignedPermission -> grantedPermissionMap.put(assignedPermission.getSid(), new PermissionVO(assignedPermission)));
            grantedPermissionMap.values().forEach(grantedPermission -> {
                if (Objects.nonNull(grantedPermission.getPid())) {
                    grantedPermissionMap.get(grantedPermission.getPid()).addSubPermission(grantedPermission);
                }
            });
            accountDetail.setGrantedPermissions(grantedPermissionMap.values().stream()
                    .filter(grantedPermission -> Permission.LEVEL_TOP == grantedPermission.getLevel())
                    .collect(Collectors.toList()));
        }
        return accountDetail;
    }

    public Account createAccount(AccountCreationVO accountCreationVO) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限创建账户");
        }
        if (WebUtils.isCompany()) {
            accountCreationVO.setType(Account.Type.COMPANY.getCode());
            accountCreationVO.setCompanyId(WebUtils.getCompanyId());
        }

        Account account = new Account();
        account.setLoginName(accountCreationVO.getLoginName());
        account.setNickName(accountCreationVO.getNickName());
        account.setRealName(accountCreationVO.getRealName());
        account.setMobile(accountCreationVO.getMobile());
        account.setEmail(accountCreationVO.getEmail());
        account.setGender(EnumUtil.codeOf(Gender.class, accountCreationVO.getGender()));
        account.setType(EnumUtil.codeOf(Account.Type.class, accountCreationVO.getType()));
        account.setStatus(EnumUtil.codeOf(Account.Status.class, accountCreationVO.getStatus()));

        account.setPassword(encoder.encode(accountCreationVO.getMobile()));

        account.setCompanyInfoSid(accountCreationVO.getCompanyId());
        String loginName = WebUtils.getLoginName();
        account.setCreatedBy(loginName);
        account.setCreatedTime(new Date(System.currentTimeMillis()));
        account.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        createAccount(account);
        return account;
    }

    public int createAccount(Account account) {
        int insertedRows;
        try {
            insertedRows = accountMapper.create(account);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public void associateWithCompanyInfo(Long companyInfoSid) {
        Account selfAccount = findBySid(WebUtils.getUserId());
        if (Objects.isNull(selfAccount)) {
            throw new ServiceException("账号不存在，关联企业信息失败");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("companyInfoSid", companyInfoSid);
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", selfAccount.getVersionNum());
        params.put("sid", selfAccount.getSid());

        int updatedRows = updateAccount(params);
        if (updatedRows == 0) {
            throw new ServiceException("此账号正在被其他人修改，请稍后再试");
        }
    }

    public void updateAccount(Long accountSid, AccountUpdateVO accountUpdateVO) {
        if (WebUtils.isMember()) {
            if (!WebUtils.getUserId().equals(accountSid)) {
                throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限修改他人的账号信息");
            }
        }
        Account editingAccount = findBySid(accountSid);
        if (Objects.isNull(editingAccount)) {
            throw new ServiceException("ID为[" + accountSid + "]的账号不存在");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(editingAccount.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限修改其他" + Account.Type.COMPANY.getName() + "的账号信息");
            }
        }
        Account account = new Account();
        account.setSid(accountSid);
        account.setLoginName(accountUpdateVO.getLoginName());
        account.setNickName(accountUpdateVO.getNickName());
        account.setRealName(accountUpdateVO.getRealName());
        account.setMobile(accountUpdateVO.getMobile());
        account.setEmail(accountUpdateVO.getEmail());
        account.setGender(EnumUtil.codeOf(Gender.class, accountUpdateVO.getGender()));

        account.setUpdatedBy(WebUtils.getLoginName());
        account.setUpdatedTime(new Date(System.currentTimeMillis()));
        account.setVersionNum(editingAccount.getVersionNum());
        int updatedRows = updateAccount(account);
        if (updatedRows == 0) {
            throw new ServiceException("此账号正在被其他人修改，请稍后再试");
        }
    }

    public int updateAccount(Account account) {
        int updatedRows;
        try {
            updatedRows = accountMapper.updateByExample(account);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public int updateAccount(Map<String, Object> params) {
        int updatedRows;
        try {
            updatedRows = accountMapper.updateByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public void resetPassword(Long accountSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限重置密码");
        }
        Account account = findBySid(accountSid);
        if (Objects.isNull(account)) {
            throw new ServiceException("账号不存在，重置密码失败");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(account.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限重置其他" + Account.Type.COMPANY.getName() + "账号的密码");
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("password", encoder.encode(account.getMobile()));
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", account.getVersionNum());
        params.put("sid", account.getSid());

        int updatedRows = updateAccount(params);
        if (updatedRows == 0) {
            throw new ServiceException("此账号正在被其他人修改，请稍后再试");
        }
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        if (WebUtils.isEmployee()) {
            throw new ServiceException("没有权限修改非雇员的密码");
        }
        Long accountId = WebUtils.getUserId();
        Account account = findBySid(accountId);
        if (Objects.isNull(account)) {
            throw new ServiceException("账号不存在，修改密码失败");
        }

        if (StringUtils.isBlank(changePasswordRequest.getOriginPwd())) {
            throw new ServiceException("原始密码不能为空");
        }

        if (!encoder.matches(changePasswordRequest.getOriginPwd(), account.getPassword())) {
            throw new ServiceException("原始密码输入不正确");
        }

        if (StringUtils.isBlank(changePasswordRequest.getNewPwd())) {
            throw new ServiceException("新密码不能为空");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("password", encoder.encode(changePasswordRequest.getNewPwd()));
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", account.getVersionNum());
        params.put("sid", account.getSid());

        int updatedRows = updateAccount(params);
        if (updatedRows == 0) {
            throw new ServiceException("此账号正在被其他人修改，请稍后再试");
        }
    }

    public void updateAccountType(Long accountSid, String type) {
        final UpdateTypeRequest updateStatusType = new UpdateTypeRequest();
        updateStatusType.setType(type);

        updateAccountType(accountSid, updateStatusType);
    }

    public void updateAccountType(Long accountSid, UpdateTypeRequest updateTypeRequest) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("只有" + Role.Category.EMPLOYEE.getName() + "才有权限修改账号的类型");
        }
        Account account = findBySid(accountSid);
        if (Objects.isNull(account)) {
            throw new ServiceException("账号不存在，更新账号类型失败");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("type", updateTypeRequest.getType());
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", account.getVersionNum());
        params.put("sid", account.getSid());

        int updatedRows = updateAccount(params);
        if (updatedRows == 0) {
            throw new ServiceException("此账号正在被其他人修改，请稍后再试");
        }
    }

    public void updateAccountStatus(Long accountSid, UpdateStatusRequest updateStatusRequest) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限更新账号状态");
        }
        Account account = findBySid(accountSid);
        if (Objects.isNull(account)) {
            throw new ServiceException("ID为[" + accountSid + "]的账号不存在，更新状态失败");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(account.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限修改其他" + Account.Type.COMPANY.getName() + "账号的状态");
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", updateStatusRequest.getStatus());
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", account.getVersionNum());
        params.put("sid", account.getSid());

        int updatedRows = updateAccount(params);
        if (updatedRows == 0) {
            throw new ServiceException("此账号正在被其他人修改，请稍后再试");
        }
    }

    public void addRolesToGivenAccount(Long accountSid, List<Long> roleSids) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限给账号分配角色");
        }
        Account editingAccount = findBySid(accountSid);
        if (Objects.isNull(editingAccount)) {
            throw new ServiceException("ID为[" + accountSid + "]的账号不存在");
        }

        List<Role> rolesToAdd = roleService.findBySids(roleSids);
        if (Objects.isNull(rolesToAdd) || rolesToAdd.isEmpty() || rolesToAdd.size() != roleSids.size()) {
            throw new ServiceException("分配给账号的某些角色不存在，分配失败");
        }

        if (WebUtils.isCompany()) {
            final Long companyInfoSid = WebUtils.getCompanyId();
            if (!companyInfoSid.equals(editingAccount.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限给其他" + Account.Type.COMPANY.getName() + "的账号分配角色");
            }
            for (Role roleToAdd : rolesToAdd) {
                if (!companyInfoSid.equals(roleToAdd.getCompanyInfoSid())) {
                    throw new ServiceException("分配给账号的某些角色不属于该" + Account.Type.COMPANY.getName() + "，分配失败");
                }
            }
        }

        List<AccountRoleRelationship> originAccountRoleRelationships = accountRoleRelationshipService.findByAccountSid(accountSid);
        List<Long> originRoleSids = originAccountRoleRelationships.stream()
                .map(originAccountRoleRelationship -> originAccountRoleRelationship.getRoleSid())
                .collect(Collectors.toList());

        List<Long> remainRoleSids = ListUtils.getIntersection(originRoleSids, roleSids);
        List<Long> removedRoleSids = ListUtils.getDifference(originRoleSids, remainRoleSids);
        List<Long> addedRoleSids = ListUtils.getDifference(roleSids, remainRoleSids);

        for(Long removedRoleSid : removedRoleSids) {
            int deletedRows = accountRoleRelationshipService.deleteByAccountSidAndRoleSid(accountSid, removedRoleSid);
            if (deletedRows == 0) {
                throw new ServiceException("其他人正在给此账号分配角色，请稍后再试");
            }
        }

        Date createdTime = new Date(System.currentTimeMillis());
        String createdBy = WebUtils.getLoginName();
        for(Long addedRoleSid : addedRoleSids) {
            AccountRoleRelationship accountRoleRelationship = new AccountRoleRelationship();
            accountRoleRelationship.setAccountSid(accountSid);
            accountRoleRelationship.setRoleSid(addedRoleSid);
            accountRoleRelationship.setCreatedBy(createdBy);
            accountRoleRelationship.setCreatedTime(createdTime);
            accountRoleRelationship.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);
            try {
                accountRoleRelationshipService.createAccountRoleRelationship(accountRoleRelationship);
            } catch (ServiceException e) {
                throw new ServiceException("其他人正在给此账号分配角色，请稍后再试", e);
            }
        }
    }

    public Account findBySid(Long sid) {
        return findSingle("sid", sid);
    }

    public Account findByLoginName(String loginName) {
        return findSingle("loginName", loginName);
    }

    public Account findByMobile(String mobile) {
        return findSingle("mobile", mobile);
    }

    public Account findByEmail(String email) {
        return findSingle("email", email);
    }

    private Account findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<Account> matchedAccounts = findByParams(params);
        if (Objects.nonNull(matchedAccounts) && !matchedAccounts.isEmpty()) {
            return matchedAccounts.get(0);
        }
        return null;
    }

    public int countByParams(Map<String, Object> params) {
        int count;
        try {
            count = accountMapper.countByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return count;
    }

    public List<Account> findByParams(Map<String, Object> params) {
        List<Account> matchedAccounts;
        try {
            matchedAccounts = accountMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedAccounts;
    }

    private UserBasicInfo toUserBasicInfo(Account account) {
        return new UserBasicInfo.Builder()
                .id(account.getSid())
                .loginName(account.getLoginName())
                .mobile(account.getMobile())
                .email(account.getEmail())
                .type(Role.Category.ACCOUNT.getCode())
                .subType(account.getType().getCode())
                .password(account.getPassword())
                .status(account.getStatus().getCode())
                .realName(account.getRealName())
                .nickName(account.getNickName())
                .gender(account.getGender().getCode())
                .headImgUrl(account.getHeadImgUrl())
                .companyId(Objects.isNull(account.getCompanyInfoSid()) ? -99 : account.getCompanyInfoSid())
                .build();
    }

    public UserBasicInfo loadUserBasicInfoByMobile(String mobile) {
        Account account = findByMobile(mobile);
        if (Objects.isNull(account)) {
            throw new UsernameNotFoundException("手机号[" + mobile + "]不存在");
        }
        return toUserBasicInfo(account);
    }

    public UserBasicInfo loadUserBasicInfoByUserName(String username) {
        Account account = findByLoginName(username);
        if (Objects.isNull(account)) {
            account = findByMobile(username);
        }
        if (Objects.isNull(account)) {
            account = findByEmail(username);
        }
        if (Objects.isNull(account)) {
            throw new UsernameNotFoundException("用户名[" + username + "]不存在");
        }
        if (Account.Status.ACTIVE != account.getStatus()) {
            throw new DisabledException("账号处于" + account.getStatus().getName() + "状态，认证失败");
        }
        return toUserBasicInfo(account);
    }

    public List<GrantedPermission> loadGrantedApiListByGrantedRoles(List<GrantedRole> grantedRoles) {
        final List<Permission> matchedApiList = permissionService.findPermissionsOfGivenRoles(grantedRoles.stream()
                .map(role -> role.getId())
                .collect(Collectors.toList()), Permission.Type.API);
        if (Objects.nonNull(matchedApiList) && !matchedApiList.isEmpty()) {

            List<GrantedPermission> grantedApiList = matchedApiList.stream()
                    .filter(matchedApi -> Permission.Status.ENABLED == matchedApi.getStatus())
                    .map(matchedApi -> new GrantedPermission.Builder()
                            .id(matchedApi.getSid())
                            .name(matchedApi.getName())
                            .title(matchedApi.getTitle())
                            .path(matchedApi.getPath())
                            .build())
                    .collect(Collectors.toList());

            return grantedApiList;
        }
        return null;
    }

    public List<GrantedPermission> loadGrantedPermissionsByGrantedRoles(List<GrantedRole> grantedRoles) {
        final List<Permission> matchedPermissions = permissionService.findPermissionsOfGivenRoles(grantedRoles.stream()
                .map(role -> role.getId())
                .collect(Collectors.toList()), Permission.Type.PAGE);

        if (Objects.nonNull(matchedPermissions) && !matchedPermissions.isEmpty()) {
            final Map<Long, GrantedPermission> grantedPermissionMap = new HashMap<>();
            matchedPermissions.forEach(matchedPermission -> {
                if (Permission.Status.ENABLED == matchedPermission.getStatus()) {
                    List<Role> associatedRoles = roleService.findRolesOfGivenPermission(matchedPermission.getSid());
                    grantedPermissionMap.put(matchedPermission.getSid(), new GrantedPermission.Builder()
                            .id(matchedPermission.getSid())
                            .path(matchedPermission.getPath())
                            .componentUrl(matchedPermission.getComponentUrl())
                            .noCacheFlag(matchedPermission.getNoCacheFlag())
                            .hiddenFlag(matchedPermission.getHiddenFlag())
                            .alwaysShowFlag(matchedPermission.getAlwaysShowFlag())
                            .redirectPath(matchedPermission.getRedirectPath())
                            .name(matchedPermission.getName())
                            .title(matchedPermission.getTitle())
                            .icon(matchedPermission.getIcon())
                            .level(matchedPermission.getLevel())
                            .pid(matchedPermission.getParentSid())
                            .roles(Objects.isNull(associatedRoles) ? null : associatedRoles.stream()
                                    .map(associatedRole -> new GrantedRole.Builder()
                                            .id(associatedRole.getSid())
                                            .code(associatedRole.getCode())
                                            .name(associatedRole.getName())
                                            .build())
                                    .collect(Collectors.toList()))
                            .build());
                }
            });
            grantedPermissionMap.values().forEach(grantedPermission -> {
                if (Objects.nonNull(grantedPermission.getPid())) {
                    grantedPermissionMap.get(grantedPermission.getPid()).addSubPermission(grantedPermission);
                }
            });
            return grantedPermissionMap.values().stream()
                    .filter(grantedPermission -> Permission.LEVEL_TOP == grantedPermission.getLevel())
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<GrantedRole> loadGrantedRolesByUserBasicInfo(UserBasicInfo userBasicInfo) {
        final List<Role> roles = roleService.findRolesOfGivenAccount(userBasicInfo.getId());
        if (Objects.isNull(roles) || roles.isEmpty()) {
            return Arrays.asList(new GrantedRole.Builder()
                    .id(GlobalConstant.DEFAULT_ACCOUNT_ROLE_ID)
                    .code(GlobalConstant.DEFAULT_ACCOUNT_ROLE_CODE)
                    .name(GlobalConstant.DEFAULT_ACCOUNT_ROLE_NAME)
                    .build());
        }
        return roles.stream()
                .filter(role -> Role.Status.ENABLED == role.getStatus())
                .map(role -> new GrantedRole.Builder()
                        .id(role.getSid())
                        .code(role.getCode())
                        .name(role.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public UserContext loadUserContextByUsername(String username) {
        UserBasicInfo userBasicInfo = loadUserBasicInfoByUserName(username);
        List<GrantedRole> grantedRoles = loadGrantedRolesByUserBasicInfo(userBasicInfo);
        List<GrantedPermission> grantedApiList = loadGrantedApiListByGrantedRoles(grantedRoles);
        List<GrantedPermission> grantedPermissions = loadGrantedPermissionsByGrantedRoles(grantedRoles);
        UserProfile userProfile = new UserProfile.Builder()
                .basicInfo(userBasicInfo)
                .roles(grantedRoles)
                .permissions(grantedPermissions)
                .grantedApiList(grantedApiList)
                .build();
        return new UserContext(username, userProfile);
    }

    public UserBasicInfo login(String username, String password) {
        UserBasicInfo userBasicInfo = loadUserBasicInfoByUserName(username);
        if (!encoder.matches(password, userBasicInfo.getPassword())) {
            throw new BadCredentialsException("认证失败，用户名或密码不正确");
        }
        return userBasicInfo;
    }

    public UserBasicInfo fastLogin(String mobile, String dynamicCode) {
        return null;
    }
}
