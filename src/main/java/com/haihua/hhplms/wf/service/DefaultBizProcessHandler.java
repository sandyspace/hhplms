package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.ana.entity.Account;
import com.haihua.hhplms.ana.entity.CompanyInfo;
import com.haihua.hhplms.ana.entity.Permission;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.service.AccountService;
import com.haihua.hhplms.ana.service.CompanyInfoService;
import com.haihua.hhplms.ana.service.PermissionService;
import com.haihua.hhplms.ana.service.RoleService;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.wf.entity.ProcessExecution;
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
    @Autowired
    @Qualifier("companyInfoService")
    private CompanyInfoService companyInfoService;

    @Autowired
    @Qualifier("accountService")
    private AccountService accountService;

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

            if (bizCode == BizCode.COMPLETE_COMPANY_INFO_CREATION) {
                CompanyInfo companyInfoOfInitBy = companyInfoService.findBySid(initBy.getCompanyInfoSid());
                if (Objects.isNull(companyInfoOfInitBy)) {
                    throw new ServiceException("用户名为:[" + processExecution.getInitBy() + "]的企业信息不存在，" + bizCode.getName() + "失败");
                }
                companyInfoService.updateCompanyInfoStatus(companyInfoOfInitBy.getSid(), CompanyInfo.Status.APPROVED.getCode());

                createAndAssignAdminRoleToAccount(initBy, companyInfoOfInitBy);
            }
        }
    }

    private void createAndAssignAdminRoleToAccount(Account initBy, CompanyInfo companyInfoOfInitBy) {
        Role companyTempRole = roleService.getCompanyTempRole();
        if (Objects.isNull(companyTempRole)) {
            throw new ServiceException("系统中没有配置类型为" + Role.Type.COMPANY_TEMP.getCode() + "的角色");
        }

        Role adminRole = new Role();
        adminRole.setCode(companyTempRole.getCode() + "_" + companyInfoOfInitBy.getSid());
        adminRole.setName(companyInfoOfInitBy.getName() + "管理员");
        adminRole.setType(Role.Type.PRE_ASSIGNED);
        adminRole.setCategory(Role.Category.ACCOUNT);
        adminRole.setStatus(Role.Status.ENABLED);
        adminRole.setCompanyInfoSid(companyInfoOfInitBy.getSid());

        adminRole.setCreatedBy(WebUtils.getLoginName());
        adminRole.setCreatedTime(new Date(System.currentTimeMillis()));
        adminRole.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        roleService.createRole(adminRole);

        List<Permission> permissions = permissionService.findPermissionsOfGivenRoles(Arrays.asList(companyTempRole.getSid()), Permission.Type.PAGE);
        roleService.addPermissionsToGivenRole(adminRole.getSid(),
                permissions.stream()
                        .map(permission -> permission.getSid())
                        .collect(Collectors.toList()));

        accountService.addRolesToGivenAccount(initBy.getSid(), Arrays.asList(adminRole.getSid()));
    }
}
