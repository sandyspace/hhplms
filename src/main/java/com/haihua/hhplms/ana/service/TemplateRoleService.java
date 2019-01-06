package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.TemplateRole;
import com.haihua.hhplms.ana.vo.TemplateRoleCreationVO;
import com.haihua.hhplms.ana.vo.TemplateRoleUpdateVO;
import com.haihua.hhplms.ana.vo.TemplateRoleVO;
import com.haihua.hhplms.common.model.PageWrapper;

import java.util.List;

public interface TemplateRoleService {
    PageWrapper<List<TemplateRoleVO>> listTempRoleByPage(String codeLike,
                                                         String nameLike,
                                                         String type,
                                                         Integer pageNo,
                                                         Integer pageSize);
    TemplateRoleVO loadDetail(Long sid);
    TemplateRole createTempRole(TemplateRoleCreationVO templateRoleCreationVO);
    void updateTempRole(Long tempRoleSid, TemplateRoleUpdateVO tempRoleUpdateVO);
    void removeTempRole(Long sid);
    void addPermissionsToTempRole(Long tempRoleSid, List<Long> permissionSids);
    List<TemplateRole> getAvailableTempRoles();
    TemplateRole findBySid(Long sid);
}
