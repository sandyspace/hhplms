package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.AccountRoleRelationship;

import java.util.List;
import java.util.Map;

public interface AccountRoleRelationshipService {
    int createAccountRoleRelationship(AccountRoleRelationship accountRoleRelationship);
    int deleteByAccountSidAndRoleSid(Long accountSid, Long roleSid);
    int deleteByParams(Map<String, Object> params);
    List<AccountRoleRelationship> findByAccountSid(Long accountSid);
    List<AccountRoleRelationship> findByRoleSid(Long roleSid);
    List<AccountRoleRelationship> findByParams(Map<String, Object> params);
}
