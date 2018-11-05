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

        Map<String, Object> params = new HashMap<>();
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
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            params.put("type", Account.Type.COMPANY.getCode());
            Long companyId = WebUtils.getCompanyId();
            params.put("companyInfoSid", companyId);
        } else {
            if (!StringUtils.isBlank(type)) {
                params.put("type", type);
            }
            if (Objects.nonNull(companyInfoSid)) {
                params.put("companyInfoSid", companyInfoSid);
            }
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
        Account matchedAccount = findBySid(sid);
        if (Objects.isNull(matchedAccount)) {
            throw new ServiceException("Account with id: [" + sid + "] does not exist");
        }
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            Long companyId = WebUtils.getCompanyId();
            if (!companyId.equals(matchedAccount.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to view the detail of this account");
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
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            accountCreationVO.setType(Account.Type.COMPANY.getCode());
            Long companyId = WebUtils.getCompanyId();
            accountCreationVO.setCompanyId(companyId);
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

    public void updateAccount(Long accountSid, AccountUpdateVO accountUpdateVO) {
        Account editingAccount = findBySid(accountSid);
        if (Objects.isNull(editingAccount)) {
            throw new ServiceException("Account with id: [" + accountSid + "] does not exist");
        }
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            Long companyId = WebUtils.getCompanyId();
            if (!companyId.equals(editingAccount.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to edit this account");
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
            throw new ServiceException("Account is being edited by others, please try again later");
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
        Account account = findBySid(accountSid);
        if (Objects.isNull(account)) {
            throw new ServiceException("account does not exist, failed to reset password");
        }

        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            Long companyId = WebUtils.getCompanyId();
            if (!companyId.equals(account.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to reset password of this account");
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
            throw new ServiceException("Account is being edited by others, please try again later");
        }
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        Long accountId = WebUtils.getUserId();
        Account account = findBySid(accountId);
        if (Objects.isNull(account)) {
            throw new ServiceException("account does not exist, failed to change password");
        }

        if (StringUtils.isBlank(changePasswordRequest.getOriginPwd())) {
            throw new ServiceException("origin password can not be blank");
        }

        if (!encoder.matches(changePasswordRequest.getOriginPwd(), account.getPassword())) {
            throw new ServiceException("origin password is not correct");
        }

        if (StringUtils.isBlank(changePasswordRequest.getNewPwd())) {
            throw new ServiceException("new password can not be blank");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("password", encoder.encode(changePasswordRequest.getNewPwd()));
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", account.getVersionNum());
        params.put("sid", account.getSid());

        int updatedRows = updateAccount(params);
        if (updatedRows == 0) {
            throw new ServiceException("Account is being edited by others, please try again later");
        }
    }

    public void updateAccountType(Long accountSid, UpdateTypeRequest updateTypeRequest) {
        Account account = findBySid(accountSid);
        if (Objects.isNull(account)) {
            throw new ServiceException("account does not exist, failed to change type");
        }
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            Long companyId = WebUtils.getCompanyId();
            if (!companyId.equals(account.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to update type of this account");
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("type", updateTypeRequest.getType());
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", account.getVersionNum());
        params.put("sid", account.getSid());

        int updatedRows = updateAccount(params);
        if (updatedRows == 0) {
            throw new ServiceException("Account is being edited by others, please try again later");
        }
    }

    public void updateAccountStatus(Long accountSid, UpdateStatusRequest updateStatusRequest) {
        Account account = findBySid(accountSid);
        if (Objects.isNull(account)) {
            throw new ServiceException("account does not exist, failed to change status");
        }
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            Long companyId = WebUtils.getCompanyId();
            if (!companyId.equals(account.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to update type of this account");
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
            throw new ServiceException("Account is being edited by others, please try again later");
        }
    }

    public void addRolesToGivenAccount(Long accountSid, List<Long> roleSids) {
        Account editingAccount = findBySid(accountSid);
        if (Objects.isNull(editingAccount)) {
            throw new ServiceException("Account with id: [" + accountSid + "] does not exit");
        }

        List<Role> rolesToAdd = roleService.findBySids(roleSids);
        if (Objects.isNull(rolesToAdd) || rolesToAdd.isEmpty() || rolesToAdd.size() != roleSids.size()) {
            throw new ServiceException("some roles to be added to this account do not exist");
        }

        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            Long companyId = WebUtils.getCompanyId();
            if (!companyId.equals(editingAccount.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to add roles to this account");
            }

            for (Role roleToAdd : rolesToAdd) {
                if (!companyId.equals(roleToAdd.getCompanyInfoSid())) {
                    throw new ServiceException("You have insufficient right to add these roles to this account");
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
                throw new ServiceException("Account is being edited by others, please try again later");
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
                throw new ServiceException("Account is being edited by others, please try again later", e);
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

    public UserBasicInfo loadUserBasicInfoByMobile(String mobile) {
        Account account = findByMobile(mobile);
        if (Objects.isNull(account)) {
            throw new UsernameNotFoundException("Username[" + mobile + "] does not exist");
        }
        return new UserBasicInfo.Builder()
                .loginName(account.getLoginName())
                .mobile(account.getMobile())
                .email(account.getEmail())
                .password(account.getPassword())
                .status(account.getStatus().getCode())
                .realName(account.getRealName())
                .nickName(account.getNickName())
                .gender(account.getGender().getCode())
                .headImgUrl(account.getHeadImgUrl())
                .companyId(account.getCompanyInfoSid())
                .build();
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
            throw new UsernameNotFoundException("Username[" + username + "] does not exist");
        }
        if (Account.Status.ACTIVE != account.getStatus()) {
            throw new DisabledException("Authentication Failed. Your account was in " + account.getStatus().getCode() + " status");
        }
        return new UserBasicInfo.Builder()
                .loginName(account.getLoginName())
                .mobile(account.getMobile())
                .email(account.getEmail())
                .type(Role.Category.ACCOUNT.getCode())
                .password(account.getPassword())
                .status(account.getStatus().getCode())
                .realName(account.getRealName())
                .nickName(account.getNickName())
                .gender(account.getGender().getCode())
                .headImgUrl(account.getHeadImgUrl())
                .companyId(account.getCompanyInfoSid())
                .build();
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
            throw new InsufficientAuthenticationException("User has no roles assigned");
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
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }
        return userBasicInfo;
    }

    public UserBasicInfo fastLogin(String mobile, String dynamicCode) {
        return null;
    }
}
