package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.vo.PermissionVO;
import com.haihua.hhplms.ana.entity.Permission;
import com.haihua.hhplms.ana.vo.PermissionVO;

import java.util.List;
import java.util.Map;

public interface PermissionService {
    List<PermissionVO> loadPermissions();
    List<PermissionVO> loadApiList();
    List<PermissionVO> getPermissionsOfGivenRole(Long roleSid);
    List<PermissionVO> getApiListOfGivenRole(Long roleSid);
    List<Permission> findPermissionsOfGivenRoles(List<Long> roleSids, Permission.Type type);
    List<Permission> findPermissionsOfGivenRoles(List<Long> roleSids);
    List<Permission> findByParams(Map<String, Object> params);
}
