package com.haihua.hhplms.security.model;

import com.haihua.hhplms.common.model.InstanceBuilder;

import java.util.List;

public class UserProfile {
    private UserBasicInfo basicInfo;
    private List<GrantedRole> roles;
    private List<GrantedPermission> permissions;
    private List<GrantedPermission> grantedApiList;

    private UserProfile(Builder userProfileBuilder) {
        this.basicInfo = userProfileBuilder.basicInfo;
        this.roles = userProfileBuilder.roles;
        this.permissions = userProfileBuilder.permissions;
        this.grantedApiList = userProfileBuilder.grantedApiList;
    }

    public UserBasicInfo getBasicInfo() {
        return basicInfo;
    }

    public List<GrantedRole> getRoles() {
        return roles;
    }

    public List<GrantedPermission> getPermissions() {
        return permissions;
    }

    public List<GrantedPermission> getGrantedApiList() {
        return grantedApiList;
    }

    public static class Builder implements InstanceBuilder<UserProfile> {
        private UserBasicInfo basicInfo;
        private List<GrantedRole> roles;
        private List<GrantedPermission> permissions;
        private List<GrantedPermission> grantedApiList;

        public Builder basicInfo(UserBasicInfo basicInfo) {
            this.basicInfo = basicInfo;
            return this;
        }

        public Builder roles(List<GrantedRole> roles) {
            this.roles = roles;
            return this;
        }

        public Builder permissions(List<GrantedPermission> permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder grantedApiList(List<GrantedPermission> grantedApiList) {
            this.grantedApiList = grantedApiList;
            return this;
        }

        @Override
        public UserProfile build() {
            return new UserProfile(this);
        }
    }
}
