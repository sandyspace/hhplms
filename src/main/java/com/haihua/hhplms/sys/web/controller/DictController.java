package com.haihua.hhplms.sys.web.controller;

import com.haihua.hhplms.ana.entity.*;
import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.pm.entity.PreferentialMsg;
import com.haihua.hhplms.sys.model.DictEntry;
import com.haihua.hhplms.wf.entity.ProcessExecution;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DictController {

    @GetMapping(path = "/api/sys/genders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadGenders() {
        List<DictEntry> genders = Arrays.stream(Gender.values())
                .map(gender -> new DictEntry(gender.getCode(), gender.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(genders, "");
    }

    @GetMapping(path = "/api/sys/account/types", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadAccountTypes() {
        List<DictEntry> accountTypes = Arrays.stream(Account.Type.values())
                .map(accountType -> new DictEntry(accountType.getCode(), accountType.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(accountTypes, "");
    }

    @GetMapping(path = "/api/sys/account/statuses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadAccountStatuses() {
        List<DictEntry> accountStatuses = Arrays.stream(Account.Status.values())
                .map(accountStatus -> new DictEntry(accountStatus.getCode(), accountStatus.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(accountStatuses, "");
    }

    @GetMapping(path = "/api/sys/employee/statuses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadEmployeeStatuses() {
        List<DictEntry> employeeStatuses = Arrays.stream(Employee.Status.values())
                .map(employeeStatus -> new DictEntry(employeeStatus.getCode(), employeeStatus.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(employeeStatuses, "");
    }

    @GetMapping(path = "/api/sys/permission/types", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadPermissionTypes() {
        List<DictEntry> permissionTypes = Arrays.stream(Permission.Type.values())
                .map(permissionType -> new DictEntry(permissionType.getCode(), permissionType.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(permissionTypes, "");
    }

    @GetMapping(path = "/api/sys/permission/statuses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadPermissionStatuses() {
        List<DictEntry> permissionStatuses = Arrays.stream(Permission.Status.values())
                .map(permissionStatus -> new DictEntry(permissionStatus.getCode(), permissionStatus.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(permissionStatuses, "");
    }

    @GetMapping(path = "/api/sys/role/categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadRoleCategories() {
        List<DictEntry> roleCategories = Arrays.stream(Role.Category.values())
                .map(roleCategory -> new DictEntry(roleCategory.getCode(), roleCategory.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(roleCategories, "");
    }

    @GetMapping(path = "/api/sys/role/types", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadRoleTypes() {
        List<DictEntry> roleTypes = Arrays.stream(Role.Type.values())
                .map(roleType -> new DictEntry(roleType.getCode(), roleType.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(roleTypes, "");
    }

    @GetMapping(path = "/api/sys/role/statuses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadRoleStatuses() {
        List<DictEntry> roleStatuses = Arrays.stream(Role.Status.values())
                .map(roleStatus -> new DictEntry(roleStatus.getCode(), roleStatus.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(roleStatuses, "");
    }

    @GetMapping(path = "/api/sys/process/statuses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadProcessStatuses() {
        List<DictEntry> processStatuses = Arrays.stream(ProcessExecution.ProcessStatus.values())
                .map(processStatus -> new DictEntry(processStatus.getCode(), processStatus.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(processStatuses, "");
    }

    @GetMapping(path = "/api/sys/step/statuses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadStepStatuses() {
        List<DictEntry> stepStatuses = Arrays.stream(ProcessExecution.StepStatus.values())
                .map(stepStatus -> new DictEntry(stepStatus.getCode(), stepStatus.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(stepStatuses, "");
    }

    @GetMapping(path = "/api/sys/companyInfo/types", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadCompanyInfoTypes() {
        List<DictEntry> companyInfoTypes = Arrays.stream(CompanyInfo.Type.values())
                .map(companyInfoType -> new DictEntry(companyInfoType.getCode(), companyInfoType.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(companyInfoTypes, "");
    }

    @GetMapping(path = "/api/sys/companyInfo/statuses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadCompanyInfoStatuses() {
        List<DictEntry> companyInfoStatues = Arrays.stream(CompanyInfo.Status.values())
                .map(companyInfoStatus -> new DictEntry(companyInfoStatus.getCode(), companyInfoStatus.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(companyInfoStatues, "");
    }

    @GetMapping(path = "/api/sys/preferentialMsg/statuses", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<DictEntry>> loadPreferentialMsgStatuses() {
        List<DictEntry> preferentialMsgStatues = Arrays.stream(PreferentialMsg.Status.values())
                .map(preferentialMsgStatus -> new DictEntry(preferentialMsgStatus.getCode(), preferentialMsgStatus.getName()))
                .collect(Collectors.toList());
        return ResultBean.Success.of(preferentialMsgStatues, "");
    }
}
