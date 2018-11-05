package com.haihua.hhplms.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haihua.hhplms.ana.vo.RoleVO;
import com.haihua.hhplms.common.model.InstanceBuilder;

import java.util.ArrayList;
import java.util.List;

public class GrantedPermission {
    @JsonIgnore
    private Long id;
    private String path;
    private String componentUrl;
    private String noCacheFlag;
    private String hiddenFlag;
    private String alwaysShowFlag;
    private String redirectPath;
    private String name;
    private String title;
    private String icon;
    private Integer level;
    @JsonIgnore
    private Long pid;
    private List<GrantedPermission> subPermissions = new ArrayList<>();
    private List<GrantedRole> roles;

    private GrantedPermission(Builder grantedPermissionBuilder) {
        this.id = grantedPermissionBuilder.id;
        this.path = grantedPermissionBuilder.path;
        this.componentUrl = grantedPermissionBuilder.componentUrl;
        this.noCacheFlag = grantedPermissionBuilder.noCacheFlag;
        this.hiddenFlag = grantedPermissionBuilder.hiddenFlag;
        this.alwaysShowFlag = grantedPermissionBuilder.alwaysShowFlag;
        this.redirectPath = grantedPermissionBuilder.redirectPath;
        this.name = grantedPermissionBuilder.name;
        this.title = grantedPermissionBuilder.title;
        this.icon = grantedPermissionBuilder.icon;
        this.level = grantedPermissionBuilder.level;
        this.pid = grantedPermissionBuilder.pid;
        this.roles = grantedPermissionBuilder.roles;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getComponentUrl() {
        return componentUrl;
    }

    public String getNoCacheFlag() {
        return noCacheFlag;
    }

    public String getHiddenFlag() {
        return hiddenFlag;
    }

    public String getAlwaysShowFlag() {
        return alwaysShowFlag;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public Integer getLevel() {
        return level;
    }

    public Long getPid() {
        return pid;
    }

    public void addSubPermission(GrantedPermission subPermission) {
        subPermissions.add(subPermission);
    }

    public List<GrantedPermission> getSubPermissions() {
        return subPermissions;
    }

    public List<GrantedRole> getRoles() {
        return roles;
    }

    public static class Builder implements InstanceBuilder<GrantedPermission> {
        private Long id;
        private String path;
        private String componentUrl;
        private String noCacheFlag;
        private String hiddenFlag;
        private String alwaysShowFlag;
        private String redirectPath;
        private String name;
        private String title;
        private String icon;
        private Integer level;
        private Long pid;
        private List<GrantedRole> roles;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder componentUrl(String componentUrl) {
            this.componentUrl = componentUrl;
            return this;
        }

        public Builder noCacheFlag(String noCacheFlag) {
            this.noCacheFlag = noCacheFlag;
            return this;
        }

        public Builder hiddenFlag(String hiddenFlag) {
            this.hiddenFlag = hiddenFlag;
            return this;
        }

        public Builder alwaysShowFlag(String alwaysShowFlag) {
            this.alwaysShowFlag = alwaysShowFlag;
            return this;
        }

        public Builder redirectPath(String redirectPath) {
            this.redirectPath = redirectPath;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder level(Integer level) {
            this.level = level;
            return this;
        }

        public Builder pid(Long pid) {
            this.pid = pid;
            return this;
        }

        public Builder roles(List<GrantedRole> roles) {
            this.roles = roles;
            return this;
        }

        @Override
        public GrantedPermission build() {
            return new GrantedPermission(this);
        }
    }
}
