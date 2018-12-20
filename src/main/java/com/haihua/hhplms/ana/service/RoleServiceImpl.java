package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.vo.RoleCreationVO;
import com.haihua.hhplms.ana.vo.RoleUpdateVO;
import com.haihua.hhplms.ana.vo.RoleVO;
import com.haihua.hhplms.ana.entity.*;
import com.haihua.hhplms.ana.mapper.RoleMapper;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.JsonUtil;
import com.haihua.hhplms.common.utils.ListUtils;
import com.haihua.hhplms.common.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    @Qualifier("accountService")
    private AccountService accountService;

    @Autowired
    @Qualifier("employeeRoleRelationshipService")
    private EmployeeRoleRelationshipService employeeRoleRelationshipService;

    @Autowired
    @Qualifier("accountRoleRelationshipService")
    private AccountRoleRelationshipService accountRoleRelationshipService;

    @Autowired
    @Qualifier("rolePermissionRelationshipService")
    private RolePermissionRelationshipService rolePermissionRelationshipService;

    public PageWrapper<List<RoleVO>> listRoleByPage(String codeLike,
                                                    String nameLike,
                                                    String category,
                                                    String type,
                                                    Long companyInfoSid,
                                                    Integer pageNo,
                                                    Integer pageSize) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看");
        }
        Map<String, Object> params = new HashMap<>();
        if (WebUtils.isEmployee()) {
            if (!StringUtils.isBlank(category)) {
                params.put("category", category);
            }
            if (Objects.nonNull(companyInfoSid)) {
                params.put("companyInfoSid", companyInfoSid);
            }
        } else {
            params.put("category", Role.Category.ACCOUNT.getCode());
            params.put("companyInfoSid", WebUtils.getCompanyId());
        }
        if (!StringUtils.isBlank(codeLike)) {
            params.put("codeLike", codeLike);
        }
        if (!StringUtils.isBlank(nameLike)) {
            params.put("nameLike", nameLike);
        }
        if (!StringUtils.isBlank(type)) {
            params.put("type", type);
        }
        return listRoleByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<RoleVO>> listRoleByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<RoleVO>> pageOfRoles = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageSize);
            List<Role> matchedRoles = findByParams(params);
            pageOfRoles.setResult(matchedRoles.stream()
                    .map(matchedRole -> new RoleVO(matchedRole))
                    .collect(Collectors.toList()));
        }
        return pageOfRoles;
    }

    public RoleVO loadDetail(Long sid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看");
        }
        Role role = findBySid(sid);
        if (Objects.isNull(role)) {
            throw new ServiceException("ID为[" + sid + "]的角色不存在");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(role.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限查看其他" + Account.Type.COMPANY.getName() + "的角色信息");
            }
        }
        return new RoleVO(role);
    }

    public List<RoleVO> getAvailableRoles() {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", Role.Status.ENABLED);
        List<Role> roles = findByParams(params);
        if (Objects.nonNull(roles) && !roles.isEmpty()) {
            return roles.stream().map(role -> new RoleVO(role)).collect(Collectors.toList());
        }
        return null;
    }

    public List<RoleVO> getAvailableRolesOfCompany(Long companyInfoSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限获取");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("status", Role.Status.ENABLED);
        params.put("category", Role.Category.ACCOUNT.getCode());
        if (WebUtils.isEmployee()) {
            params.put("companyInfoSid", companyInfoSid);
        } else {
            params.put("companyInfoSid", WebUtils.getCompanyId());
        }
        List<Role> roles = findByParams(params);
        if (Objects.nonNull(roles) && !roles.isEmpty()) {
            return roles.stream().map(role -> new RoleVO(role)).collect(Collectors.toList());
        }
        return null;
    }

    public List<RoleVO> getRolesOfGivenEmployee(Long employeeSid) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }
        List<Role> roles = findRolesOfGivenEmployee(employeeSid);
        if (Objects.nonNull(roles) && !roles.isEmpty()) {
            return roles.stream().map(role -> new RoleVO(role)).collect(Collectors.toList());
        }
        return null;
    }

    public List<RoleVO> getRolesOfGivenAccount(Long accountSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限获取");
        }
        if (WebUtils.isCompany()) {
            Account account = accountService.findBySid(accountSid);
            if (Objects.isNull(account)) {
                throw new ServiceException("ID为[" + accountSid + "]的账号不存在");
            }
            if (!WebUtils.getCompanyId().equals(account.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限查看其他" + Account.Type.COMPANY.getName() + "账号的角色信息");
            }
        }

        List<Role> roles = findRolesOfGivenAccount(accountSid);
        if (Objects.nonNull(roles) && !roles.isEmpty()) {
            return roles.stream().map(role -> new RoleVO(role)).collect(Collectors.toList());
        }
        return null;
    }

    public Role createRole(RoleCreationVO roleCreationVO) {
        if (log.isInfoEnabled()) {
            log.info(String.format("roleCreationVO: %s, createdBy: {loginName: %s, userType: %s, subType: %s}", roleCreationVO, WebUtils.getLoginName(), WebUtils.getUserType(), WebUtils.getSubType()));
        }

        if (WebUtils.isEmployee()) {
            if (Role.Category.EMPLOYEE == EnumUtil.codeOf(Role.Category.class, roleCreationVO.getCategory())) {
                if (Role.Type.PRE_ASSIGNED == EnumUtil.codeOf(Role.Type.class, roleCreationVO.getType())) {
                    throw new ServiceException(String.format("不能创建类型为%s的角色", Role.Type.PRE_ASSIGNED.getName()));
                }
                roleCreationVO.setCompanyId(null);
            } else {
                if (Role.Type.COMPANY_TEMP == EnumUtil.codeOf(Role.Type.class, roleCreationVO.getType())) {
                    throw new ServiceException(String.format("不能为%s创建类型为%s的角色", Account.Type.COMPANY.getName(), Role.Type.COMPANY_TEMP.getName()));
                }
            }
        } else {
            if (WebUtils.isMember()) {
                throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限创建");
            }
            roleCreationVO.setCategory(Role.Category.ACCOUNT.getCode());
            roleCreationVO.setType(Role.Type.CREATED.getCode());
            roleCreationVO.setCompanyId(WebUtils.getCompanyId());
        }

        if (codeExist(roleCreationVO.getCode())) {
            throw new ServiceException(String.format("编码[%s]已被占用", roleCreationVO.getCode()));
        }

        Role role = new Role();
        role.setCode(roleCreationVO.getCode());
        role.setName(roleCreationVO.getName());
        role.setCategory(EnumUtil.codeOf(Role.Category.class, roleCreationVO.getCategory()));
        role.setType(EnumUtil.codeOf(Role.Type.class, roleCreationVO.getType()));
        Long companyId = WebUtils.getCompanyId();
        role.setCompanyInfoSid(companyId);
        role.setStatus(Role.Status.ENABLED);
        role.setCompanyInfoSid(roleCreationVO.getCompanyId());
        role.setMemo(roleCreationVO.getMemo());
        role.setCreatedBy(WebUtils.getLoginName());
        role.setCreatedTime(new Date(System.currentTimeMillis()));
        role.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);
        createRole(role);
        return role;
    }

    public int createRole(Role role) {
        int insertedRows;
        try {
            insertedRows = roleMapper.create(role);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public void updateRole(Long roleSid, RoleUpdateVO roleUpdateVO) {
        if (log.isInfoEnabled()) {
            log.info(String.format("roleSid: %d, roleUpdateVO: %s, updatedBy: {loginName: %s, userType: %s, subType: %s}", roleSid, roleUpdateVO, WebUtils.getLoginName(), WebUtils.getUserType(), WebUtils.getSubType()));
        }
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限更新");
        }

        Role editingRole = findBySid(roleSid);
        if (Objects.isNull(editingRole)) {
            throw new ServiceException(String.format("ID为[%d]的角色不存在", roleSid));
        }

        if (WebUtils.isEmployee()) {
            if (Role.Category.EMPLOYEE == editingRole.getCategory() && Role.Type.PRE_ASSIGNED == editingRole.getType()) {
                throw new ServiceException(String.format("不能修改%s类型角色", Role.Type.PRE_ASSIGNED.getName()));
            }
        }

        if (WebUtils.isCompany()) {
            if (Role.Type.CREATED != editingRole.getType()) {
                throw new ServiceException("不能修改此类型角色");
            }
            if (!WebUtils.getCompanyId().equals(editingRole.getCompanyInfoSid())) {
                throw new ServiceException(String.format("%s没有权限修改其他%s的角色信息", Account.Type.COMPANY.getName(), Account.Type.COMPANY.getName()));
            }
        }

        Role example = new Role();
        example.setSid(roleSid);
        if (!StringUtils.isBlank(roleUpdateVO.getName())) {
            example.setName(roleUpdateVO.getName());
        }
        example.setMemo(roleUpdateVO.getMemo());
        example.setUpdatedBy(WebUtils.getLoginName());
        example.setUpdatedTime(new Date(System.currentTimeMillis()));
        example.setVersionNum(editingRole.getVersionNum());
        int updatedRows = updateRole(example);
        if (updatedRows == 0) {
            throw new ServiceException("此角色正在被其他人修改，请稍后再试");
        }
    }

    public int updateRole(Role role) {
        int updatedRows;
        try {
            updatedRows = roleMapper.updateByExample(role);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    @Transactional
    public void removeRole(Long sid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(String.format("%s没有权限删除角色", Account.Type.MEMBER.getName()));
        }
        Role role = findBySid(sid);
        if (Objects.isNull(role)) {
            throw new ServiceException(String.format("ID为[%d]的角色不存在", sid));
        }
        if (WebUtils.isEmployee()) {
            if (Role.Category.EMPLOYEE == role.getCategory() && Role.Type.PRE_ASSIGNED == role.getType()) {
                throw new ServiceException(String.format("不能删除%s类型角色", Role.Type.PRE_ASSIGNED.getName()));
            }
        }
        if (WebUtils.isCompany()) {
            if (Role.Type.CREATED != role.getType()) {
                throw new ServiceException("不能删除此类型角色");
            }
            if (!WebUtils.getCompanyId().equals(role.getCompanyInfoSid())) {
                throw new ServiceException(String.format("%s没有权限删除其他%s的角色信息", Account.Type.COMPANY.getName(), Account.Type.COMPANY.getName()));
            }
        }
        List<EmployeeRoleRelationship> employeeRoleRelations = employeeRoleRelationshipService.findByRoleSid(role.getSid());
        if (Objects.nonNull(employeeRoleRelations) && !employeeRoleRelations.isEmpty()) {
            throw new ServiceException(String.format("一个或者多个%s已分配了此角色", Role.Category.EMPLOYEE.getName()));
        }
        if (Role.Category.ACCOUNT == role.getCategory()) {
            List<AccountRoleRelationship> accountRoleRelations = accountRoleRelationshipService.findByRoleSid(role.getSid());
            if (Objects.nonNull(accountRoleRelations) && !accountRoleRelations.isEmpty()) {
                throw new ServiceException(String.format("一个或者多个%s已分配了此角色", Account.Type.COMPANY.getName()));
            }
        }

        rolePermissionRelationshipService.deleteByRoleSid(role.getSid());
        if (log.isInfoEnabled()) {
            log.info(String.format("menus and apis related to role with id: [%d] has been deleted by {loginName: %s, userType: %s, subType: %s}", role.getSid(), WebUtils.getLoginName(), WebUtils.getUserType(), WebUtils.getSubType()));
        }

        deleteBySid(role.getSid());
        if (log.isInfoEnabled()) {
            log.info(String.format("role %s has been deleted by {loginName: %s, userType: %s, subType: %s}", JsonUtil.toJson(role), WebUtils.getLoginName(), WebUtils.getUserType(), WebUtils.getSubType()));
        }
    }

    public int deleteBySid(Long sid) {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        return deleteByParams(params);
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = roleMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }

    @Transactional
    public void addPermissionsToRole(Long roleSid, List<Long> permissionSids) {
        if (log.isInfoEnabled()) {
            log.info(String.format("roleSid: %d, permissionSids: %s, addedBy: {loginName: %s, userType: %s, subType: %s}",
                    roleSid, StringUtils.join(permissionSids, ","), WebUtils.getLoginName(), WebUtils.getUserType(), WebUtils.getSubType()));
        }
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限给角色分配菜单或者API");
        }
        if (Objects.isNull(permissionSids) || permissionSids.isEmpty()) {
            return;
        }
        Role role = findBySid(roleSid);
        if (Objects.isNull(role)) {
            throw new ServiceException("ID为[" + roleSid + "]的角色不存在");
        }

        if (WebUtils.isEmployee()) {
            if (Role.Category.EMPLOYEE == role.getCategory() && Role.Type.PRE_ASSIGNED == role.getType()) {
                throw new ServiceException(String.format("不能给%s类型角色分配菜单或者API", Role.Type.PRE_ASSIGNED.getName()));
            }
        }

        if (WebUtils.isCompany()) {
            if (Role.Type.PRE_ASSIGNED == role.getType()) {
                throw new ServiceException(String.format("不能给%s类型角色分配菜单或者API", Role.Type.PRE_ASSIGNED.getName()));
            }
            if (!WebUtils.getCompanyId().equals(role.getCompanyInfoSid())) {
                throw new ServiceException(String.format("%s没有权限给其他%s的角色分配菜单或者API", Account.Type.COMPANY.getName(), Account.Type.COMPANY.getName()));
            }
            List<Role> preassignedRoles = getPreassignedRoles(WebUtils.getCompanyId());
            if (Objects.isNull(preassignedRoles) || preassignedRoles.isEmpty()) {
                throw new ServiceException(String.format("请联系系统管理员创建%s类型角色", Role.Type.PRE_ASSIGNED.getName()));
            }

            List<RolePermissionRelationship> preassignedRolePermissionRelationships = rolePermissionRelationshipService.findByRoleSids(preassignedRoles.stream()
                    .map(preassignedRole -> preassignedRole.getSid())
                    .collect(Collectors.toList()));

            Set<Long> preassignedPermissionSids = preassignedRolePermissionRelationships.stream()
                    .map(preassignedRolePermissionRelationship -> preassignedRolePermissionRelationship.getPermissionSid())
                    .collect(Collectors.toSet());

            if (!preassignedPermissionSids.containsAll(permissionSids)) {
                throw new ServiceException("待分配的菜单或者API已经超出授权给你们的范围");
            }
        }

        addPermissionsToGivenRole(roleSid, permissionSids);
    }

    public void addPermissionsToGivenRole(Long roleSid, List<Long> permissionSids) {
        List<RolePermissionRelationship> originRolePermissionRelationships = rolePermissionRelationshipService.findByRoleSid(roleSid);
        List<Long> originPermissionSids = originRolePermissionRelationships.stream()
                .map(originRolePermissionRelationship -> originRolePermissionRelationship.getPermissionSid())
                .collect(Collectors.toList());
        List<Long> remainPermissionSids = ListUtils.getIntersection(originPermissionSids, permissionSids);
        List<Long> removedPermissionSids = ListUtils.getDifference(originPermissionSids, remainPermissionSids);
        List<Long> addedPermissionSids = ListUtils.getDifference(permissionSids, remainPermissionSids);

        for (Long removedPermissionSid : removedPermissionSids) {
            int deletedRows = rolePermissionRelationshipService.deleteByRoleSidAndPermissionSid(roleSid, removedPermissionSid);
            if (deletedRows == 0) {
                throw new ServiceException("其他人正在给该角色分配菜单或者API，请稍后再试");
            }
        }

        Date createdTime = new Date(System.currentTimeMillis());
        String createdBy = WebUtils.getLoginName();
        for (Long addedPermissionSid : addedPermissionSids) {
            RolePermissionRelationship rolePermissionRelationship = new RolePermissionRelationship();
            rolePermissionRelationship.setRoleSid(roleSid);
            rolePermissionRelationship.setPermissionSid(addedPermissionSid);
            rolePermissionRelationship.setCreatedBy(createdBy);
            rolePermissionRelationship.setCreatedTime(createdTime);
            rolePermissionRelationship.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);
            try {
                rolePermissionRelationshipService.createRolePermissionRelationship(rolePermissionRelationship);
            } catch (Exception e) {
                throw new ServiceException("其他人正在给该角色分配菜单或者API，请稍后再试");
            }
        }
    }

    public Role findBySid(Long sid) {
        if (Objects.isNull(sid)) {
            return null;
        }
        return findSingle("sid", sid);
    }

    public Role findByCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        return findSingle("code", code);
    }

    private boolean codeExist(String code) {
        Role role = findByCode(code);
        return Objects.nonNull(role);
    }

    private Role findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<Role> matchedRoles = findByParams(params);
        if (Objects.nonNull(matchedRoles) && !matchedRoles.isEmpty()) {
            return matchedRoles.get(0);
        }
        return null;
    }
    public int countByParams(Map<String, Object> params) {
        int count;
        try {
            count = roleMapper.countByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return count;
    }

    public List<Role> findByParams(Map<String, Object> params) {
        List<Role> matchedRoles;
        try {
            matchedRoles = roleMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedRoles;
    }

    public List<Role> getTempRoles() {
        Map<String, Object> params = new HashMap<>();
        params.put("category", Role.Category.EMPLOYEE.getCode());
        params.put("type", Role.Type.COMPANY_TEMP.getCode());
        params.put("status", Role.Status.ENABLED.getCode());
        return findByParams(params);
    }

    public List<Role> getPreassignedRoles(Long companyInfoSid) {
        if (Objects.isNull(companyInfoSid)) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("category", Role.Category.ACCOUNT.getCode());
        params.put("type", Role.Type.PRE_ASSIGNED.getCode());
        params.put("status", Role.Status.ENABLED.getCode());
        params.put("companyInfoSid", companyInfoSid);
        return findByParams(params);
    }

    public List<Role> findBySids(List<Long> searchingSids) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchingSids", searchingSids);
        return findByParams(params);
    }

    public List<Role> findRolesOfGivenPermission(Long permissionSid) {
        List<RolePermissionRelationship> matchedRolePermissionRelationships = rolePermissionRelationshipService.findByPermissionSid(permissionSid);
        if (Objects.nonNull(matchedRolePermissionRelationships) && !matchedRolePermissionRelationships.isEmpty()) {
            return findBySids(matchedRolePermissionRelationships.stream()
                    .map(matchedRolePermissionRelationship -> matchedRolePermissionRelationship.getRoleSid())
                    .collect(Collectors.toList()));
        }
        return null;
    }

    public List<Role> findRolesOfGivenEmployee(Long employeeSid) {
        List<EmployeeRoleRelationship> matchedEmployeeRoleRelationships = employeeRoleRelationshipService.findByEmployeeSid(employeeSid);
        if (Objects.nonNull(matchedEmployeeRoleRelationships) && !matchedEmployeeRoleRelationships.isEmpty()) {
            return findBySids(matchedEmployeeRoleRelationships.stream()
                    .map(matchedEmployeeRoleRelationship -> matchedEmployeeRoleRelationship.getRoleSid())
                    .collect(Collectors.toList()));
        }
        return null;
    }

    @Override
    public List<Role> findRolesOfGivenAccount(Long accountSid) {
        List<AccountRoleRelationship> matchedAccountRoleRelationships = accountRoleRelationshipService.findByAccountSid(accountSid);
        if (Objects.nonNull(matchedAccountRoleRelationships) && !matchedAccountRoleRelationships.isEmpty()) {
            return findBySids(matchedAccountRoleRelationships.stream()
                    .map(accountRoleRelationship -> accountRoleRelationship.getRoleSid())
                    .collect(Collectors.toList()));
        }
        return null;
    }
}
