package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.RolePermissionRelationship;
import com.haihua.hhplms.ana.entity.TemplateRolePermissionRelationship;
import com.haihua.hhplms.ana.mapper.TemplateRolePermissionRelationshipMapper;
import com.haihua.hhplms.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("templateRolePermissionRelationshipService")
public class TemplateRolePermissionRelationshipServiceImpl implements TemplateRolePermissionRelationshipService {

    @Autowired
    private TemplateRolePermissionRelationshipMapper templateRolePermissionRelationshipMapper;

    public int createTempRolePermissionRelationship(TemplateRolePermissionRelationship tempRolePermissionRelationship) {
        int insertedRows;
        try {
            insertedRows = templateRolePermissionRelationshipMapper.create(tempRolePermissionRelationship);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public List<TemplateRolePermissionRelationship> findByTempRoleSids(List<Long> tempRoleSids) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchingTemplateRoleSids", tempRoleSids);
        return findByParams(params);
    }

    public List<TemplateRolePermissionRelationship> findByTempRoleSid(Long tempRoleSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("templateRoleSid", tempRoleSid);
        return findByParams(params);
    }

    public List<TemplateRolePermissionRelationship> findByParams(Map<String, Object> params) {
        List<TemplateRolePermissionRelationship> matchedTempRolePermissionRelationships;
        try {
            matchedTempRolePermissionRelationships = templateRolePermissionRelationshipMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedTempRolePermissionRelationships;
    }

    public int deleteByTempRoleSidAndPermissionSid(Long tempRoleSid, Long permissionSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("templateRoleSid", tempRoleSid);
        params.put("permissionSid", permissionSid);
        return deleteByParams(params);
    }

    public int deleteByTempRoleSid(Long tempRoleSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("templateRoleSid", tempRoleSid);
        return deleteByParams(params);
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = templateRolePermissionRelationshipMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }
}
