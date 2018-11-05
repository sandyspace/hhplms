package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.Permission;
import com.haihua.hhplms.ana.entity.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionMapper {
    List<Permission> findByParams(Map<String, Object> params);
}
