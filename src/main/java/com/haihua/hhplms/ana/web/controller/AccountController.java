package com.haihua.hhplms.ana.web.controller;

import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
import com.haihua.hhplms.ana.vo.UpdateTypeRequest;
import com.haihua.hhplms.ana.entity.Account;
import com.haihua.hhplms.ana.service.AccountService;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.common.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.*;

@RestController
public class AccountController {
    @Autowired
    @Qualifier("accountService")
    private AccountService accountService;

    @GetMapping(path = "/api/ana/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<PageWrapper<List<AccountVO>>> loadAccounts(@RequestParam(name = "loginName", required = false) String loginNameLike,
                                                        @RequestParam(name = "realName", required = false) String realNameLike,
                                                        @RequestParam(name = "mobile", required = false) String mobileLike,
                                                        @RequestParam(name = "gender", required = false) String gender,
                                                        @RequestParam(name = "type", required = false) String type,
                                                        @RequestParam(name = "status", required = false) String status,
                                                        @RequestParam(name = "companyId", required = false) Long companyInfoSid,
                                                        @RequestParam(name = "pageNo") Integer pageNo,
                                                        @RequestParam(name = "pageSize") Integer pageSize) {
        PageWrapper<List<AccountVO>> pageOfAccounts = accountService.loadAccountsByPage(loginNameLike, realNameLike, mobileLike, gender, type, status, companyInfoSid, pageNo, pageSize);
        return ResultBean.Success.of(pageOfAccounts, "");
    }

    @GetMapping(path = "/api/ana/accounts/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<AccountVO> loadDetail(@PathVariable("id") Long sid) {
        AccountVO accountDetail = accountService.loadDetail(sid);
        return ResultBean.Success.of(accountDetail, "");
    }

    @PostMapping(path = "/api/ana/accounts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> createAccount(@RequestBody AccountCreationVO accountCreationVO) {
        Account createdAccount = accountService.createAccount(accountCreationVO);
        return ResultBean.Success.of(createdAccount.getSid(), "");
    }

    @PutMapping(path = "/api/ana/account/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateSelfInfo(@RequestBody AccountUpdateVO accountUpdateVO) {
        accountService.updateAccount(WebUtils.getUserId(), accountUpdateVO);
        return ResultBean.Success.of(null, "");
    }

    @PutMapping(path = "/api/ana/accounts/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateAccount(@PathVariable("id") Long sid,
                                                  @RequestBody AccountUpdateVO accountUpdateVO) {
        accountService.updateAccount(sid, accountUpdateVO);
        return ResultBean.Success.of(sid, "");
    }

    @PostMapping(path = "/api/ana/accounts/{id}/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> addRolesToAccount(@PathVariable("id") Long accountSid,
                                                      @RequestBody List<Long> roleSids) {
        accountService.addRolesToAccount(accountSid, roleSids);
        return ResultBean.Success.of(accountSid, "");
    }

    @PatchMapping(path = "/api/ana/accounts/{id}/resetPwd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> resetPwd(@PathVariable("id") Long accountSid) {
        accountService.resetPassword(accountSid);
        return ResultBean.Success.of(accountSid, "");
    }

    @PatchMapping(path = "/api/ana/accounts/changePwd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<String> changePwd(@RequestBody ChangePasswordRequest changePasswordRequest) {
        accountService.changePassword(changePasswordRequest);
        return ResultBean.Success.of("", "");
    }

    @PatchMapping(path = "/api/ana/accounts/{id}/updateType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateAccountType(@PathVariable("id") Long accountSid,
                                                      @RequestBody UpdateTypeRequest updateTypeRequest) {
        accountService.updateAccountType(accountSid, updateTypeRequest);
        return ResultBean.Success.of(accountSid, "");
    }

    @PatchMapping(path = "/api/ana/accounts/{id}/updateStatus", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateAccountStatus(@PathVariable("id") Long accountSid,
                                                        @RequestBody UpdateStatusRequest updateStatusRequest) {
        accountService.updateAccountStatus(accountSid, updateStatusRequest);
        return ResultBean.Success.of(accountSid, "");
    }
}
