package com.haihua.hhplms.security.auth.ajax;

import com.haihua.hhplms.security.model.GrantedPermission;
import com.haihua.hhplms.security.model.GrantedRole;
import com.haihua.hhplms.security.model.UserBasicInfo;
import com.haihua.hhplms.security.model.UserContext;
import com.haihua.hhplms.security.model.GrantedPermission;
import com.haihua.hhplms.security.model.GrantedRole;
import com.haihua.hhplms.security.model.UserBasicInfo;
import com.haihua.hhplms.security.model.UserContext;

import java.util.List;

public interface AjaxAuthenticationService {
    UserBasicInfo login(String username, String password);
    UserBasicInfo loadUserBasicInfoByUserName(String username);
    List<GrantedRole> loadGrantedRolesByUserBasicInfo(UserBasicInfo userBasicInfo);
    List<GrantedPermission> loadGrantedApiListByGrantedRoles(List<GrantedRole> grantedRoles);
    List<GrantedPermission> loadGrantedPermissionsByGrantedRoles(List<GrantedRole> grantedRoles);
    UserContext loadUserContextByUsername(String username);
}
