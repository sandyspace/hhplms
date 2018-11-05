package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.EmployeeRoleRelationship;

import java.util.List;
import java.util.Map;

public interface EmployeeRoleRelationshipMapper {
    int create(EmployeeRoleRelationship employeeRoleRelationship);
    int deleteByParams(Map<String, Object> params);
    List<EmployeeRoleRelationship> findByParams(Map<String, Object> params);
}
