package com.haihua.hhplms.ana.vo;


import com.haihua.hhplms.ana.entity.Permission;

import java.util.ArrayList;
import java.util.List;

public class PermissionVO {
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
    private String status;
    private Long pid;
    private Integer versionNum;
    private List<PermissionVO> subPermissions = new ArrayList<>();

    public PermissionVO(Permission permission) {
        this.id = permission.getSid();
        this.path = permission.getPath();
        this.componentUrl = permission.getComponentUrl();
        this.noCacheFlag = permission.getNoCacheFlag();
        this.hiddenFlag = permission.getHiddenFlag();
        this.alwaysShowFlag = permission.getAlwaysShowFlag();
        this.redirectPath = permission.getRedirectPath();
        this.name = permission.getName();
        this.title = permission.getTitle();
        this.icon = permission.getIcon();
        this.level = permission.getLevel();
        this.status = permission.getStatus().getCode();
        this.pid = permission.getParentSid();
        this.versionNum = permission.getVersionNum();
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

    public String getStatus() {
        return status;
    }

    public Long getPid() {
        return pid;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public List<PermissionVO> getSubPermissions() {
        return subPermissions;
    }

    public void setSubPermissions(List<PermissionVO> subPermissions) {
        this.subPermissions = subPermissions;
    }

    public void addSubPermission(PermissionVO subPermission) {
        subPermissions.add(subPermission);
    }
}
