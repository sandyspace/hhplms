package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.ana.entity.Account;
import com.haihua.hhplms.ana.entity.CompanyInfo;
import com.haihua.hhplms.ana.service.AccountService;
import com.haihua.hhplms.ana.service.CompanyInfoService;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.wf.entity.ProcessExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("defaultBizProcessHandler")
public class DefaultBizProcessHandler implements BizProcessHandler {
    @Autowired
    @Qualifier("companyInfoService")
    private CompanyInfoService companyInfoService;

    @Autowired
    @Qualifier("accountService")
    private AccountService accountService;

    public void handleBizProcess(BizCode bizCode, ProcessExecution processExecution) {
        if (bizCode == BizCode.COMPLETE_COMPANY_INFO_CREATION) {
            Account initBy = accountService.findByLoginName(processExecution.getInitBy());
            if (Objects.isNull(initBy)) {
                throw new ServiceException("启动流程用户名为:[" + processExecution.getInitBy() + "]的账号不存在，" + bizCode.getName() + "失败");
            }
            accountService.updateAccountType(initBy.getSid(), Account.Type.COMPANY.getCode());

            CompanyInfo companyInfoOfInitBy = companyInfoService.findBySid(initBy.getCompanyInfoSid());
            if (Objects.isNull(companyInfoOfInitBy)) {
                throw new ServiceException("用户名为:[" + processExecution.getInitBy() + "]的企业信息不存在，" + bizCode.getName() + "失败");
            }
            companyInfoService.updateCompanyInfoStatus(companyInfoOfInitBy.getSid(), CompanyInfo.Status.APPROVED.getCode());
        }
    }
}
