package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.Account;
import com.haihua.hhplms.ana.vo.PermissionVO;
import com.haihua.hhplms.ana.entity.Permission;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.entity.RolePermissionRelationship;
import com.haihua.hhplms.ana.mapper.PermissionMapper;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    @Qualifier("roleService")
    private RoleService roleService;

    @Autowired
    @Qualifier("rolePermissionRelationshipService")
    private RolePermissionRelationshipService rolePermissionRelationshipService;

    public List<PermissionVO> permissionsAvailableToAssign(Long refRoleSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看");
        }
        final List<Permission> availablePermissions = availablePermissions(refRoleSid, Permission.Type.PAGE);
        final Map<Long, PermissionVO> availablePermissionMap = new HashMap<>();
        availablePermissions.forEach(availablePermission -> availablePermissionMap.put(availablePermission.getSid(), new PermissionVO(availablePermission)));
        availablePermissionMap.values().forEach(permission -> {
            if (Objects.nonNull(permission.getPid())) {
                availablePermissionMap.get(permission.getPid()).addSubPermission(permission);
            }
        });
        return availablePermissionMap.values().stream()
                .filter(availablePermission -> Permission.LEVEL_TOP == availablePermission.getLevel())
                .collect(Collectors.toList());
    }

    public List<PermissionVO> apisAvailableToAssign(Long refRoleSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看");
        }
        final List<Permission> availableApis = availablePermissions(refRoleSid, Permission.Type.API);
        return availableApis.stream()
                .map(api -> new PermissionVO(api))
                .collect(Collectors.toList());
    }

    private List<Permission> availablePermissions(Long refRoleSid, Permission.Type type) {
        List<Permission> availablePermissions;
        if (WebUtils.isCompany()) {
            List<Role> preassignedRoles = roleService.getPreassignedRoles(WebUtils.getCompanyId());
            if (Objects.isNull(preassignedRoles) || preassignedRoles.isEmpty()) {
                throw new ServiceException("请联系系统管理员创建" + Role.Type.PRE_ASSIGNED.getName() + "类型角色");
            }
            availablePermissions = findPermissionsOfGivenRoles(preassignedRoles.stream()
                    .map(preassignedRole -> preassignedRole.getSid())
                    .collect(Collectors.toList()), type);
        } else {
            Role refRole = roleService.findBySid(refRoleSid);
            if (Objects.isNull(refRole)) {
                throw new ServiceException("无法获取可分配菜单或API");
            }
            if (Role.Category.ACCOUNT == refRole.getCategory() && Role.Type.CREATED == refRole.getType()) {
                List<Role> preassignedRoles = roleService.getPreassignedRoles(refRole.getCompanyInfoSid());
                if (Objects.isNull(preassignedRoles) || preassignedRoles.isEmpty()) {
                    throw new ServiceException("ID为[" + refRole.getCompanyInfoSid() + "]企业没有对应的" + Role.Type.PRE_ASSIGNED.getName() + "类型角色，请先创建");
                }
                availablePermissions = findPermissionsOfGivenRoles(preassignedRoles.stream()
                        .map(preassignedRole -> preassignedRole.getSid())
                        .collect(Collectors.toList()), type);
            } else {
                availablePermissions = findByType(type);
            }
        }
        return availablePermissions;
    }

    public List<PermissionVO> getPermissionsOfGivenRole(Long roleSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看");
        }
        Role role = roleService.findBySid(roleSid);
        if (Objects.isNull(role)) {
            throw new ServiceException("ID为[" + roleSid + "]的角色不存在");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(role.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限获取其他" + Account.Type.COMPANY.getName() + "的角色已分配菜单");
            }
        }

        List<Permission> permissions = findPermissionsOfGivenRoles(Arrays.asList(roleSid), Permission.Type.PAGE);
        if (Objects.nonNull(permissions) && !permissions.isEmpty()) {
            final Map<Long, PermissionVO> permissionMap = new HashMap<>();
            permissions.forEach(permission -> permissionMap.put(permission.getSid(), new PermissionVO(permission)));
            permissionMap.values().forEach(permission -> {
                if (Objects.nonNull(permission.getPid())) {
                    permissionMap.get(permission.getPid()).addSubPermission(permission);
                }
            });
            return permissionMap.values().stream()
                    .filter(grantedPermission -> Permission.LEVEL_TOP == grantedPermission.getLevel())
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<PermissionVO> getApiListOfGivenRole(Long roleSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看");
        }
        Role role = roleService.findBySid(roleSid);
        if (Objects.isNull(role)) {
            throw new ServiceException("ID为[" + roleSid + "]的角色不存在");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(role.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限获取其他" + Account.Type.COMPANY.getName() + "的角色已分配API");
            }
        }

        List<Permission> apiList = findPermissionsOfGivenRoles(Arrays.asList(roleSid), Permission.Type.API);
        if (Objects.nonNull(apiList) && !apiList.isEmpty()) {
            return apiList.stream()
                        .map(api -> new PermissionVO(api))
                        .collect(Collectors.toList());
        }
        return null;
    }

    public List<Permission> findPermissionsOfGivenRoles(List<Long> roleSids, Permission.Type type) {
        List<RolePermissionRelationship> matchedRolePermissionRelationships = rolePermissionRelationshipService.findByRoleSids(roleSids);
        if (Objects.nonNull(matchedRolePermissionRelationships) && !matchedRolePermissionRelationships.isEmpty()) {
            Map<String, Object> params = new HashMap<>();
            if (Objects.nonNull(type)) {
                params.put("type", type.getCode());
            }
            params.put("searchingSids", matchedRolePermissionRelationships
                    .stream()
                    .map(rolePermissionRelationship -> rolePermissionRelationship.getPermissionSid())
                    .collect(Collectors.toList()));
            return findByParams(params);
        }
        return null;
    }

    public List<Permission> findPermissionsOfGivenRoles(List<Long> roleSids) {
        return findPermissionsOfGivenRoles(roleSids, null);
    }

    public List<Permission> findByType(Permission.Type type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type.getCode());
        return findByParams(params);
    }

    public List<Permission> findByParams(Map<String, Object> params) {
        List<Permission> matchedPermissions;
        try {
            matchedPermissions = permissionMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedPermissions;
    }
}
