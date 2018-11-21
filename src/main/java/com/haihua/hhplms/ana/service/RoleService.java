package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.vo.RoleCreationVO;
import com.haihua.hhplms.ana.vo.RoleUpdateVO;
import com.haihua.hhplms.ana.vo.RoleVO;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.vo.RoleCreationVO;
import com.haihua.hhplms.ana.vo.RoleUpdateVO;
import com.haihua.hhplms.ana.vo.RoleVO;
import com.haihua.hhplms.common.model.PageWrapper;

import java.util.List;
import java.util.Map;

public interface RoleService {
    PageWrapper<List<RoleVO>> listRoleByPage(String codeLike,
                                             String nameLike,
                                             String category,
                                             String type,
                                             Long companyInfoSid,
                                             Integer pageNo,
                                             Integer pageSize);
    RoleVO loadDetail(Long sid);
    List<RoleVO> getAvailableRoles();
    List<RoleVO> getAvailableRolesOfCompany(Long distributorInfoSid);
    List<RoleVO> getRolesOfGivenEmployee(Long employeeSid);
    List<RoleVO> getRolesOfGivenAccount(Long accountSid);
    Role createRole(RoleCreationVO roleCreationVO);
    int createRole(Role role);
    void updateRole(Long roleSid, RoleUpdateVO roleUpdateVO);
    int updateRole(Role role);
    int deleteBySid(Long sid);
    int deleteByParams(Map<String, Object> params);
    void addPermissionsToRole(Long roleSid, List<Long> permissionSids);
    void addPermissionsToGivenRole(Long roleSid, List<Long> permissionSids);
    Role findBySid(Long sid);
    List<Role> findBySids(List<Long> searchingSids);
    List<Role> findByParams(Map<String, Object> params);
    List<Role> findRolesOfGivenPermission(Long permissionSid);
    List<Role> findRolesOfGivenEmployee(Long employeeSid);
    List<Role> findRolesOfGivenAccount(Long accountSid);
    Role getPreassignedRole(Long companyInfoSid);
}
