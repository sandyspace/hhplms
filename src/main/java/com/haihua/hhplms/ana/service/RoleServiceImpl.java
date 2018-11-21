package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.vo.RoleCreationVO;
import com.haihua.hhplms.ana.vo.RoleUpdateVO;
import com.haihua.hhplms.ana.vo.RoleVO;
import com.haihua.hhplms.ana.entity.*;
import com.haihua.hhplms.ana.mapper.RoleMapper;
import com.haihua.hhplms.ana.vo.RoleCreationVO;
import com.haihua.hhplms.ana.vo.RoleUpdateVO;
import com.haihua.hhplms.ana.vo.RoleVO;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.ListUtils;
import com.haihua.hhplms.common.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

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
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isBlank(codeLike)) {
            params.put("codeLike", codeLike);
        }
        if (!StringUtils.isBlank(nameLike)) {
            params.put("nameLike", nameLike);
        }
        if (!StringUtils.isBlank(type)) {
            params.put("type", type);
        }

        String userType = WebUtils.getUserType();
        if (Role.Category.EMPLOYEE.getCode().equals(userType)) {
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
        return listRoleByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<RoleVO>> listRoleByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<RoleVO>> pageOfRoles = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageNo * pageSize);
            List<Role> matchedRoles = findByParams(params);
            pageOfRoles.setResult(matchedRoles.stream()
                    .map(matchedRole -> new RoleVO(matchedRole))
                    .collect(Collectors.toList()));
        }
        return pageOfRoles;
    }

    public RoleVO loadDetail(Long sid) {
        Role role = findBySid(sid);
        if (Objects.isNull(role)) {
            throw new ServiceException("Role with id: [" + sid + "] does not exist");
        }
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            Long companyId = WebUtils.getCompanyId();
            if (!companyId.equals(role.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to view this role");
            }
        }
        return new RoleVO(role);
    }

    public List<RoleVO> getAvailableRoles() {
        if (Role.Category.ACCOUNT.getCode().equals(WebUtils.getUserType())) {
            throw new ServiceException("You have insufficient right to view these roles");
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
        Map<String, Object> params = new HashMap<>();
        params.put("status", Role.Status.ENABLED);
        params.put("category", Role.Category.ACCOUNT.getCode());
        if (Role.Category.EMPLOYEE.getCode().equals(WebUtils.getUserType())) {
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
        List<Role> roles = findRolesOfGivenEmployee(employeeSid);
        if (Objects.nonNull(roles) && !roles.isEmpty()) {
            return roles.stream().map(role -> new RoleVO(role)).collect(Collectors.toList());
        }
        return null;
    }

    public List<RoleVO> getRolesOfGivenAccount(Long accountSid) {
        List<Role> roles = findRolesOfGivenAccount(accountSid);
        if (Objects.nonNull(roles) && !roles.isEmpty()) {
            return roles.stream().map(role -> new RoleVO(role)).collect(Collectors.toList());
        }
        return null;
    }

    public Role createRole(RoleCreationVO roleCreationVO) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            roleCreationVO.setCategory(Role.Category.ACCOUNT.getCode());
            roleCreationVO.setType(Role.Type.CREATED.getCode());
            roleCreationVO.setCompanyId(WebUtils.getCompanyId());
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
        Role editingRole = findBySid(roleSid);
        //校验待修改的role是否存在
        if (Objects.isNull(editingRole)) {
            throw new ServiceException("Role with id: [" + roleSid + "] does not exist");
        }

        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            if (Role.Type.PRE_ASSIGNED == editingRole.getType()) {
                throw new ServiceException("Pre-assigned role cannot be edited");
            }

            //校验待修改的role是否属于当前用户
            Long companyId = WebUtils.getCompanyId();
            if (!companyId.equals(editingRole.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to edit this role");
            }

            roleUpdateVO.setCategory(null);
            roleUpdateVO.setType(null);
            roleUpdateVO.setCompanyId(null);
        }

        Role role = new Role();
        role.setSid(roleSid);
        if (!StringUtils.isBlank(roleUpdateVO.getCode())) {
            role.setCode(roleUpdateVO.getCode());
        }
        if (!StringUtils.isBlank(roleUpdateVO.getName())) {
            role.setName(roleUpdateVO.getName());
        }
        if (!StringUtils.isBlank(roleUpdateVO.getCategory())) {
            role.setCategory(EnumUtil.codeOf(Role.Category.class, roleUpdateVO.getCategory()));
        }
        if (!StringUtils.isBlank(roleUpdateVO.getType())) {
            role.setType(EnumUtil.codeOf(Role.Type.class, roleUpdateVO.getType()));
        }
        if (Objects.nonNull(roleUpdateVO.getCompanyId())) {
            role.setCompanyInfoSid(roleUpdateVO.getCompanyId());
        }
        String loginName = WebUtils.getLoginName();
        role.setUpdatedBy(loginName);
        role.setUpdatedTime(new Date(System.currentTimeMillis()));
        role.setVersionNum(editingRole.getVersionNum());
        int updatedRows = updateRole(role);
        if (updatedRows == 0) {
            throw new ServiceException("Role is being edited by others, please try again later");
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

    public void addPermissionsToRole(Long roleSid, List<Long> permissionSids) {
        if (Objects.isNull(permissionSids) || permissionSids.isEmpty()) {
            return;
        }
        Role role = findBySid(roleSid);
        if (Objects.isNull(role)) {
            throw new ServiceException("Role with id: [" + roleSid + "] does not exist");
        }
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            if (Role.Type.PRE_ASSIGNED == role.getType()) {
                throw new ServiceException("Pre-assigned role cannot be edited");
            }
            Long companyId = WebUtils.getCompanyId();
            if (!companyId.equals(role.getCompanyInfoSid())) {
                throw new ServiceException("You have insufficient right to edit this role");
            }
            Role preassignedRole = getPreassignedRole(WebUtils.getCompanyId());
            if (Objects.isNull(preassignedRole)) {
                throw new ServiceException("You have no right to operation role, please contact system admin");
            }

            List<RolePermissionRelationship> preassignedRolePermissionRelationships = rolePermissionRelationshipService.findByRoleSid(preassignedRole.getSid());
            List<Long> preassignedPermissionSids = preassignedRolePermissionRelationships.stream()
                    .map(preassignedRolePermissionRelationship -> preassignedRolePermissionRelationship.getPermissionSid())
                    .collect(Collectors.toList());
            if (!preassignedPermissionSids.containsAll(permissionSids)) {
                throw new ServiceException("You have insufficient right to add these permissions to the role");
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
                throw new ServiceException("Role is being edited by others, please try again later");
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
                throw new ServiceException("Role is being edited by others, please try again later");
            }
        }
    }

    public Role findBySid(Long sid) {
        if (Objects.isNull(sid)) {
            return null;
        }
        return findSingle("sid", sid);
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

    public Role getPreassignedRole(Long companyInfoSid) {
        if (Objects.isNull(companyInfoSid)) {
            return null;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("category", Role.Category.ACCOUNT.getCode());
        params.put("type", Role.Type.PRE_ASSIGNED.getCode());
        params.put("companyInfoSid", companyInfoSid);
        List<Role> matchedRoles = findByParams(params);
        if (Objects.nonNull(matchedRoles) && !matchedRoles.isEmpty()) {
            return matchedRoles.get(0);
        }
        return null;
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
