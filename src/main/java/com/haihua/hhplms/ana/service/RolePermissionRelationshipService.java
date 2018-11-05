package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.RolePermissionRelationship;

import java.util.List;
import java.util.Map;

public interface RolePermissionRelationshipService {
    int createRolePermissionRelationship(RolePermissionRelationship rolePermissionRelationship);
    int deleteByRoleSidAndPermissionSid(Long roleSid, Long permissionSid);
    int deleteByParams(Map<String, Object> params);
    List<RolePermissionRelationship> findByRoleSids(List<Long> roleSids);
    List<RolePermissionRelationship> findByRoleSid(Long roleSid);
    List<RolePermissionRelationship> findByPermissionSid(Long permissionSid);
    List<RolePermissionRelationship> findByParams(Map<String, Object> params);
}
