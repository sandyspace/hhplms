package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.ana.entity.*;
import com.haihua.hhplms.ana.service.*;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.wf.entity.ProcessExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("defaultBizProcessHandler")
public class DefaultBizProcessHandler implements BizProcessHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultBizProcessHandler.class);

    @Autowired
    @Qualifier("companyInfoService")
    private CompanyInfoService companyInfoService;

    @Autowired
    @Qualifier("accountService")
    private AccountService accountService;

    @Autowired
    @Qualifier("templateRoleService")
    private TemplateRoleService templateRoleService;

    @Autowired
    @Qualifier("roleService")
    private RoleService roleService;

    @Autowired
    @Qualifier("permissionService")
    private PermissionService permissionService;

    public void handleBizProcess(BizCode bizCode, ProcessExecution processExecution) {
        if (bizCode == BizCode.COMPLETE_COMPANY_INFO_CREATION || bizCode == BizCode.COMPLETE_COMPANY_JOINING) {

            Account initBy = accountService.findByLoginName(processExecution.getInitBy());
            if (Objects.isNull(initBy)) {
                throw new ServiceException("启动流程用户名为:[" + processExecution.getInitBy() + "]的账号不存在，" + bizCode.getName() + "失败");
            }
            accountService.updateAccountType(initBy.getSid(), Account.Type.COMPANY.getCode());

            if (log.isInfoEnabled()) {
                log.info(String.format("update account type to: %s for account: %s", Account.Type.COMPANY.getCode(), initBy.getLoginName()));
            }

            if (bizCode == BizCode.COMPLETE_COMPANY_INFO_CREATION) {
                CompanyInfo companyInfoOfInitBy = companyInfoService.findBySid(initBy.getCompanyInfoSid());
                if (Objects.isNull(companyInfoOfInitBy)) {
                    throw new ServiceException("用户名为:[" + processExecution.getInitBy() + "]的企业信息不存在，" + bizCode.getName() + "失败");
                }
                companyInfoService.updateCompanyInfoStatus(companyInfoOfInitBy.getSid(), CompanyInfo.Status.APPROVED.getCode());

                if (log.isInfoEnabled()) {
                    log.info(String.format("update status to: %s for company: %s", CompanyInfo.Status.APPROVED.getCode(), companyInfoOfInitBy.getName()));
                }

                createAndAssignRolesToAccount(initBy, companyInfoOfInitBy);
            }
        }
    }

    private void createAndAssignRolesToAccount(Account initBy, CompanyInfo companyInfoOfInitBy) {
        List<TemplateRole> tempRoles = templateRoleService.getAvailableTempRoles();
        if (Objects.isNull(tempRoles) || tempRoles.isEmpty()) {
            throw new ServiceException("系统中没有配置模板角色");
        }

        for (TemplateRole tempRole : tempRoles) {
            Role preassignedRole = new Role();
            preassignedRole.setCode(tempRole.getCode() + "_" + companyInfoOfInitBy.getSid());
            preassignedRole.setName(tempRole.getName());
            preassignedRole.setType(Role.Type.PRE_ASSIGNED);
            preassignedRole.setCategory(Role.Category.ACCOUNT);
            preassignedRole.setStatus(Role.Status.ENABLED);
            preassignedRole.setCompanyInfoSid(companyInfoOfInitBy.getSid());

            preassignedRole.setCreatedBy(WebUtils.getLoginName());
            preassignedRole.setCreatedTime(new Date(System.currentTimeMillis()));
            preassignedRole.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);
            roleService.createRole(preassignedRole);

            if (log.isInfoEnabled()) {
                log.info(String.format("create role: %s for company owner: %s", preassignedRole, initBy.getLoginName()));
            }

            List<Permission> permissions = permissionService.findPermissionsOfTempRoles(Arrays.asList(tempRole.getSid()), Permission.Type.PAGE);
            if (Objects.nonNull(permissions) && !permissions.isEmpty()) {
                roleService.addPermissionsToGivenRole(preassignedRole.getSid(),
                        permissions.stream()
                                .map(permission -> permission.getSid())
                                .collect(Collectors.toList()));

                if (log.isInfoEnabled()) {
                    log.info("assign menus: [%s] to role: %s", permissions.stream().map(permission -> permission.getName()).collect(Collectors.joining(", ")), preassignedRole.getCode());
                }
            }

            if (TemplateRole.Type.OWNER == tempRole.getType()) {
                accountService.addRolesToGivenAccount(initBy.getSid(), Arrays.asList(preassignedRole.getSid()));

                if (log.isInfoEnabled()) {
                    log.info(String.format("add role: %s to company owner: %s", preassignedRole.getCode(), initBy.getLoginName()));
                }
            }
        }
    }
}
