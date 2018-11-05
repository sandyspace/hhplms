package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.RolePermissionRelationship;
import com.haihua.hhplms.ana.mapper.RolePermissionRelationshipMapper;
import com.haihua.hhplms.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("rolePermissionRelationshipService")
public class RolePermissionRelationshipServiceImpl implements RolePermissionRelationshipService {
    @Autowired
    private RolePermissionRelationshipMapper rolePermissionRelationshipMapper;

    public int createRolePermissionRelationship(RolePermissionRelationship rolePermissionRelationship) {
        int insertedRows;
        try {
            insertedRows = rolePermissionRelationshipMapper.create(rolePermissionRelationship);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public int deleteByRoleSidAndPermissionSid(Long roleSid, Long permissionSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleSid", roleSid);
        params.put("permissionSid", permissionSid);
        return deleteByParams(params);
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = rolePermissionRelationshipMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }

    public List<RolePermissionRelationship> findByRoleSids(List<Long> roleSids) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchingRoleSids", roleSids);
        return findByParams(params);
    }

    public List<RolePermissionRelationship> findByRoleSid(Long roleSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleSid", roleSid);
        return findByParams(params);
    }

    public List<RolePermissionRelationship> findByPermissionSid(Long permissionSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("permissionSid", permissionSid);
        return findByParams(params);
    }

    public List<RolePermissionRelationship> findByParams(Map<String, Object> params) {
        List<RolePermissionRelationship> matchedRolePermissionRelationships;
        try {
            matchedRolePermissionRelationships = rolePermissionRelationshipMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedRolePermissionRelationships;
    }
}
