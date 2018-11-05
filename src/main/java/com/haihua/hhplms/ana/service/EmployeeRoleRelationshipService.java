package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.EmployeeRoleRelationship;

import java.util.List;
import java.util.Map;

public interface EmployeeRoleRelationshipService {
    int createEmployeeRoleRelationship(EmployeeRoleRelationship employeeRoleRelationship);
    int deleteByEmployeeSidAndRoleSid(Long employeeSid, Long roleSid);
    List<EmployeeRoleRelationship> findByEmployeeSid(Long employeeSid);
    List<EmployeeRoleRelationship> findByRoleSid(Long roleSid);
    List<EmployeeRoleRelationship> findByParams(Map<String, Object> params);
}
