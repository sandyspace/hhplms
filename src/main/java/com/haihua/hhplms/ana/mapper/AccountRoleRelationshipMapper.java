package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.AccountRoleRelationship;

import java.util.List;
import java.util.Map;

public interface AccountRoleRelationshipMapper {
    int create(AccountRoleRelationship accountRoleRelationship);
    int deleteByParams(Map<String, Object> params);
    List<AccountRoleRelationship> findByParams(Map<String, Object> params);
}
