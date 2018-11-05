package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.vo.PermissionVO;
import com.haihua.hhplms.ana.entity.Permission;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.entity.RolePermissionRelationship;
import com.haihua.hhplms.ana.mapper.PermissionMapper;
import com.haihua.hhplms.ana.vo.PermissionVO;
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

    public List<PermissionVO> loadPermissions() {
        List<Permission> permissions;
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            Role preassignedRole = roleService.getPreassignedRole();
            if (Objects.isNull(preassignedRole)) {
                throw new ServiceException("You have no right to operate permissions, please contact system admin");
            }
            permissions = findPermissionsOfGivenRoles(Arrays.asList(preassignedRole.getSid()), Permission.Type.PAGE);
        } else {
            permissions = findByType(Permission.Type.PAGE);
        }
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

    public List<PermissionVO> loadApiList() {
        List<Permission> apiList;
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            Role preassignedRole = roleService.getPreassignedRole();
            if (Objects.isNull(preassignedRole)) {
                throw new ServiceException("You have no right to operate api list, please contact system admin");
            }
            apiList = findPermissionsOfGivenRoles(Arrays.asList(preassignedRole.getSid()), Permission.Type.API);
        } else {
            apiList = findByType(Permission.Type.API);
        }
        return apiList.stream()
                    .map(api -> new PermissionVO(api))
                    .collect(Collectors.toList());
    }

    public List<PermissionVO> getPermissionsOfGivenRole(Long roleSid) {
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
