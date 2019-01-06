package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.TemplateRolePermissionRelationship;

import java.util.List;
import java.util.Map;

public interface TemplateRolePermissionRelationshipMapper {
    int create(TemplateRolePermissionRelationship templateRolePermissionRelationship);
    int deleteByParams(Map<String, Object> params);
    List<TemplateRolePermissionRelationship> findByParams(Map<String, Object> params);
}
