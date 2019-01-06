package com.haihua.hhplms.ana.web.controller;

import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.entity.TemplateRole;
import com.haihua.hhplms.ana.service.TemplateRoleService;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.model.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TempRoleController {
    @Autowired
    @Qualifier("templateRoleService")
    private TemplateRoleService templateRoleService;

    @GetMapping(path = "/api/ana/tempRoles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<PageWrapper<List<TemplateRoleVO>>> loadTempRoles(
            @RequestParam(name = "code", required = false) String codeLike,
            @RequestParam(name = "name", required = false) String nameLike,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "pageNo") Integer pageNo,
            @RequestParam(name = "pageSize") Integer pageSize) {
        PageWrapper<List<TemplateRoleVO>> pageOfTempRoles = templateRoleService.listTempRoleByPage(codeLike, nameLike, type, pageNo, pageSize);
        return ResultBean.Success.of(pageOfTempRoles, "");
    }

    @GetMapping(path = "/api/ana/tempRoles/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<TemplateRoleVO> loadDetail(@PathVariable("id") Long sid) {
        return ResultBean.Success.of(templateRoleService.loadDetail(sid), "");
    }

    @PostMapping(path = "/api/ana/tempRoles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> createTempRole(@RequestBody TemplateRoleCreationVO tempRoleCreationVO) {
        TemplateRole tempRole = templateRoleService.createTempRole(tempRoleCreationVO);
        return ResultBean.Success.of(tempRole.getSid(), "");
    }

    @PutMapping(path = "/api/ana/tempRoles/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> updateTempRole(@PathVariable("id") Long sid,
                                               @RequestBody TemplateRoleUpdateVO tempRoleUpdateVO) {
        templateRoleService.updateTempRole(sid, tempRoleUpdateVO);
        return ResultBean.Success.of(sid, "");
    }

    @DeleteMapping(path = "/api/ana/tempRoles/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> removeTempRole(@PathVariable("id") Long sid) {
        templateRoleService.removeTempRole(sid);
        return ResultBean.Success.of(sid, "");
    }

    @PostMapping(path = "/api/ana/tempRoles/{id}/permissions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> addPermissionsToTempRole(@PathVariable("id") Long tempRoleSid,
                                                         @RequestBody List<Long> permissionSids) {
        templateRoleService.addPermissionsToTempRole(tempRoleSid, permissionSids);
        return ResultBean.Success.of(tempRoleSid, "");
    }
}
