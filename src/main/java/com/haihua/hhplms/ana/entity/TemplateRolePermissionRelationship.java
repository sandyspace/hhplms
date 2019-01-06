package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;

import java.util.Objects;

public class TemplateRolePermissionRelationship extends BaseEntity {
    private Long templateRoleSid;
    private Long permissionSid;

    public Long getTemplateRoleSid() {
        return templateRoleSid;
    }

    public void setTemplateRoleSid(Long templateRoleSid) {
        this.templateRoleSid = templateRoleSid;
    }

    public Long getPermissionSid() {
        return permissionSid;
    }

    public void setPermissionSid(Long permissionSid) {
        this.permissionSid = permissionSid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateRolePermissionRelationship)) return false;
        if (!super.equals(o)) return false;
        TemplateRolePermissionRelationship that = (TemplateRolePermissionRelationship) o;
        return Objects.equals(templateRoleSid, that.templateRoleSid) &&
                Objects.equals(permissionSid, that.permissionSid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), templateRoleSid, permissionSid);
    }
}
