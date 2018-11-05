package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    int create(Role role);
    int updateByExample(Role role);
    int deleteByParams(Map<String, Object> params);
    int countByParams(Map<String, Object> params);
    List<Role> findByParams(Map<String, Object> params);
}
