package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.RolePermissionRelationship;
import com.haihua.hhplms.ana.entity.RolePermissionRelationship;

import java.util.List;
import java.util.Map;

public interface RolePermissionRelationshipMapper {
    int create(RolePermissionRelationship rolePermissionRelationship);
    int deleteByParams(Map<String, Object> params);
    List<RolePermissionRelationship> findByParams(Map<String, Object> params);
}
