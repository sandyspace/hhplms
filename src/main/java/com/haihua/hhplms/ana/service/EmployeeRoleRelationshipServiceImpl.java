package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.EmployeeRoleRelationship;
import com.haihua.hhplms.ana.mapper.EmployeeRoleRelationshipMapper;
import com.haihua.hhplms.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("employeeRoleRelationshipService")
public class EmployeeRoleRelationshipServiceImpl implements EmployeeRoleRelationshipService {
    @Autowired
    private EmployeeRoleRelationshipMapper employeeRoleRelationshipMapper;

    public int createEmployeeRoleRelationship(EmployeeRoleRelationship employeeRoleRelationship) {
        int insertedRows;
        try {
            insertedRows = employeeRoleRelationshipMapper.create(employeeRoleRelationship);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public int deleteByEmployeeSidAndRoleSid(Long employeeSid, Long roleSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeSid", employeeSid);
        params.put("roleSid", roleSid);
        return deleteByParams(params);
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = employeeRoleRelationshipMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }

    public List<EmployeeRoleRelationship> findByEmployeeSid(Long employeeSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeSid", employeeSid);
        return findByParams(params);
    }

    public List<EmployeeRoleRelationship> findByRoleSid(Long roleSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleSid", roleSid);
        return findByParams(params);
    }

    public List<EmployeeRoleRelationship> findByParams(Map<String, Object> params) {
        List<EmployeeRoleRelationship> matchedEmployeeRoleRelationships;
        try {
            matchedEmployeeRoleRelationships = employeeRoleRelationshipMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedEmployeeRoleRelationships;
    }
}
