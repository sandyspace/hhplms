package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.Account;

import java.util.List;
import java.util.Map;

public interface AccountMapper {
    int create(Account account);
    int updateByParams(Map<String, Object> params);
    int updateByExample(Account account);
    int deleteByParams(Map<String, Object> params);
    int countByParams(Map<String, Object> params);
    List<Account> findByParams(Map<String, Object> params);
}
