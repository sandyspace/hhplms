package com.haihua.hhplms.ana.web.controller;

import com.haihua.hhplms.ana.vo.PermissionVO;
import com.haihua.hhplms.ana.service.PermissionService;
import com.haihua.hhplms.ana.vo.PermissionVO;
import com.haihua.hhplms.common.model.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PermissionController {

    @Autowired
    @Qualifier("permissionService")
    private PermissionService permissionService;

    @GetMapping(path = "/api/ana/permissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<PermissionVO>> permissionsAvailableToAssign(@RequestParam("refRoleId") Long refRoleSid) {
        return ResultBean.Success.of(permissionService.permissionsAvailableToAssign(refRoleSid), "");
    }

    @GetMapping(path = "/api/ana/apis", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<PermissionVO>> apisAvailableToAssign(@RequestParam("refRoleId") Long refRoleSid) {
        return ResultBean.Success.of(permissionService.apisAvailableToAssign(refRoleSid), "");
    }

    @GetMapping(path = "/api/ana/roles/{id}/permissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<PermissionVO>> getPermissionsOfRole(@PathVariable("id") Long roleSid) {
        return ResultBean.Success.of(permissionService.getPermissionsOfGivenRole(roleSid), "");
    }

    @GetMapping(path = "/api/ana/roles/{id}/apis", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<PermissionVO>> getApiListOfRole(@PathVariable("id") Long roleSid) {
        return ResultBean.Success.of(permissionService.getApiListOfGivenRole(roleSid), "");
    }

    @GetMapping(path = "/api/ana/tempRoles/permissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<PermissionVO>> permissionsAvailableToAssignTempRole() {
        return ResultBean.Success.of(permissionService.permissionsAvailableToAssignTempRole(), "");
    }

    @GetMapping(path = "/api/ana/tempRoles/apis", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<PermissionVO>> apisAvailableToAssignTempRole() {
        return ResultBean.Success.of(permissionService.apisAvailableToAssignTempRole(), "");
    }

    @GetMapping(path = "/api/ana/tempRoles/{id}/permissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<PermissionVO>> getPermissionsOfTempRole(@PathVariable("id") Long tempRoleSid) {
        return ResultBean.Success.of(permissionService.getPermissionsOfTempRole(tempRoleSid), "");
    }

    @GetMapping(path = "/api/ana/tempRoles/{id}/apis", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<PermissionVO>> getApiListOfTempRole(@PathVariable("id") Long tempRoleSid) {
        return ResultBean.Success.of(permissionService.getApiListOfTempRole(tempRoleSid), "");
    }
}
