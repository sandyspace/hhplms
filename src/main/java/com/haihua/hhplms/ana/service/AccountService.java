package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.ana.entity.Account;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.common.model.PageWrapper;

import java.util.List;
import java.util.Map;

public interface AccountService {
    PageWrapper<List<AccountVO>> loadAccountsByPage(String loginNameLike,
                                                    String realNameLike,
                                                    String mobileLike,
                                                    String gender,
                                                    String type,
                                                    String status,
                                                    Long companyInfoSid,
                                                    Integer pageNo,
                                                    Integer pageSize);
    AccountVO loadDetail(Long sid);
    AccountVO getAccount(String loginName);
    Account createAccount(AccountCreationVO accountCreationVO);
    int createAccount(Account account);
    void associateWithCompanyInfo(Long companyInfoSid);
    void updateAccount(Long accountSid, AccountUpdateVO accountUpdateVO);
    void resetPassword(Long accountSid);
    void changePassword(ChangePasswordRequest changePasswordRequest);
    void updateAccountType(Long accountSid, String type);
    void updateAccountType(Long accountSid, UpdateTypeRequest updateTypeRequest);
    void updateAccountStatus(Long accountSid, UpdateStatusRequest updateStatusRequest);
    int updateAccount(Account account);
    void addRolesToAccount(Long accountSid, List<Long> roleSids);
    void addRolesToGivenAccount(Long accountSid, List<Long> roleSids);
    Account findBySid(Long sid);
    Account findByLoginName(String loginName);
    Account findByMobile(String mobile);
    Account findByEmail(String email);
    List<Account> findByParams(Map<String, Object> params);
}
