package com.haihua.hhplms.ana.web.controller;

import com.haihua.hhplms.ana.vo.RoleCreationVO;
import com.haihua.hhplms.ana.vo.RoleUpdateVO;
import com.haihua.hhplms.ana.vo.RoleVO;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.service.RoleService;
import com.haihua.hhplms.ana.vo.RoleCreationVO;
import com.haihua.hhplms.ana.vo.RoleUpdateVO;
import com.haihua.hhplms.ana.vo.RoleVO;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.common.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class RoleController {
    @Autowired
    @Qualifier("roleService")
    private RoleService roleService;

    @GetMapping(path = "/api/ana/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<PageWrapper<List<RoleVO>>> loadRoles(
            @RequestParam(name = "code", required = false) String codeLike,
            @RequestParam(name = "name", required = false) String nameLike,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "companyId", required = false) Long companyInfoSid,
            @RequestParam(name = "pageNo") Integer pageNo,
            @RequestParam(name = "pageSize") Integer pageSize) {
        PageWrapper<List<RoleVO>> pageOfRoles = roleService.listRoleByPage(codeLike, nameLike, category, type, companyInfoSid, pageNo, pageSize);
        return ResultBean.Success.of(pageOfRoles, "");
    }

    @GetMapping(path = "/api/ana/roles/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<RoleVO> loadDetail(@PathVariable("id") Long sid) {
        return ResultBean.Success.of(roleService.loadDetail(sid), "");
    }

    @PostMapping(path = "/api/ana/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> createRole(@RequestBody RoleCreationVO roleCreationVO) {
        Role createdRole = roleService.createRole(roleCreationVO);
        return ResultBean.Success.of(createdRole.getSid(), "");
    }

    @PutMapping(path = "/api/ana/roles/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateRole(@PathVariable("id") Long sid,
                                               @RequestBody RoleUpdateVO roleUpdateVO) {
        roleService.updateRole(sid, roleUpdateVO);
        return ResultBean.Success.of(sid, "");
    }

    @DeleteMapping(path = "/api/ana/roles/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> removeRole(@PathVariable("id") Long sid) {
        roleService.removeRole(sid);
        return ResultBean.Success.of(sid, "");
    }

    @PostMapping(path = "/api/ana/roles/{id}/permissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> addPermissionsToRole(@PathVariable("id") Long roleSid,
                                                         @RequestBody List<Long> permissionSids) {
        roleService.addPermissionsToRole(roleSid, permissionSids);
        return ResultBean.Success.of(roleSid, "");
    }

    @GetMapping(path = "/api/ana/roles/available", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<RoleVO>> getAvailableRoles() {
        return ResultBean.Success.of(roleService.getAvailableRoles(), "");
    }

    @GetMapping(path = "/api/ana/companyInfos/{id}/available", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<RoleVO>> getAvailableRolesOfCompany(@PathVariable("id") Long companyInfoSid) {
        return ResultBean.Success.of(roleService.getAvailableRolesOfCompany(companyInfoSid), "");
    }

    @GetMapping(path = "/api/ana/accounts/{id}/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<RoleVO>> getRolesOfAccount(@PathVariable("id") Long accountSid) {
        return ResultBean.Success.of(roleService.getRolesOfGivenAccount(accountSid), "");
    }

    @GetMapping(path = "/api/ana/employees/{id}/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<RoleVO>> getRolesOfEmployee(@PathVariable("id") Long employeeSid) {
        return ResultBean.Success.of(roleService.getRolesOfGivenEmployee(employeeSid), "");
    }
}
