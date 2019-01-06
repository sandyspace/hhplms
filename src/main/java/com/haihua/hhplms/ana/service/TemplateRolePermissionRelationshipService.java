package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.TemplateRolePermissionRelationship;

import java.util.List;

public interface TemplateRolePermissionRelationshipService {
    int createTempRolePermissionRelationship(TemplateRolePermissionRelationship tempRolePermissionRelationship);
    List<TemplateRolePermissionRelationship> findByTempRoleSids(List<Long> tempRoleSids);
    List<TemplateRolePermissionRelationship> findByTempRoleSid(Long tempRoleSid);
    int deleteByTempRoleSid(Long tempRoleSid);
    int deleteByTempRoleSidAndPermissionSid(Long tempRoleSid, Long permissionSid);
}
