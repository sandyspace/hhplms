package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.mapper.AccountRoleRelationshipMapper;
import com.haihua.hhplms.ana.entity.AccountRoleRelationship;
import com.haihua.hhplms.ana.mapper.AccountRoleRelationshipMapper;
import com.haihua.hhplms.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("accountRoleRelationshipService")
public class AccountRoleRelationshipServiceImpl implements AccountRoleRelationshipService {
    @Autowired
    private AccountRoleRelationshipMapper accountRoleRelationshipMapper;

    public int createAccountRoleRelationship(AccountRoleRelationship accountRoleRelationship) {
        int insertedRows;
        try {
            insertedRows = accountRoleRelationshipMapper.create(accountRoleRelationship);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public int deleteByAccountSidAndRoleSid(Long accountSid, Long roleSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountSid", accountSid);
        params.put("roleSid", roleSid);
        return deleteByParams(params);
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = accountRoleRelationshipMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }

    @Override
    public List<AccountRoleRelationship> findByAccountSid(Long accountSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountSid", accountSid);
        return findByParams(params);
    }

    @Override
    public List<AccountRoleRelationship> findByRoleSid(Long roleSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleSid", roleSid);
        return findByParams(params);
    }

    @Override
    public List<AccountRoleRelationship> findByParams(Map<String, Object> params) {
        List<AccountRoleRelationship> matchedAccountRoleRelationships;
        try {
            matchedAccountRoleRelationships = accountRoleRelationshipMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedAccountRoleRelationships;
    }
}
