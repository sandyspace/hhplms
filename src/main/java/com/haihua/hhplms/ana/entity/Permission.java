package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;
import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.Objects;

public class Permission extends BaseEntity {
    public static final int LEVEL_TOP = 1;
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
    private Type type;
    private Status status;
    private Long parentSid;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponentUrl() {
        return componentUrl;
    }

    public void setComponentUrl(String componentUrl) {
        this.componentUrl = componentUrl;
    }

    public String getNoCacheFlag() {
        return noCacheFlag;
    }

    public void setNoCacheFlag(String noCacheFlag) {
        this.noCacheFlag = noCacheFlag;
    }

    public String getHiddenFlag() {
        return hiddenFlag;
    }

    public void setHiddenFlag(String hiddenFlag) {
        this.hiddenFlag = hiddenFlag;
    }

    public String getAlwaysShowFlag() {
        return alwaysShowFlag;
    }

    public void setAlwaysShowFlag(String alwaysShowFlag) {
        this.alwaysShowFlag = alwaysShowFlag;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

    public void setRedirectPath(String redirectPath) {
        this.redirectPath = redirectPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getParentSid() {
        return parentSid;
    }

    public void setParentSid(Long parentSid) {
        this.parentSid = parentSid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        if (!super.equals(o)) return false;
        Permission that = (Permission) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name);
    }

    public enum Type implements BaseEnum {
        PAGE("page", "页面"),
        API("api", "接口");

        private String code;
        private String name;

        Type(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    public enum Status implements BaseEnum {
        ENABLED("enabled", "启用"),
        DISABLED("disabled", "禁用");

        private String code;
        private String name;

        Status(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}
