package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;

import java.util.Objects;

public class RolePermissionRelationship extends BaseEntity {
    private Long roleSid;
    private Long permissionSid;

    public Long getRoleSid() {
        return roleSid;
    }

    public void setRoleSid(Long roleSid) {
        this.roleSid = roleSid;
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
        if (!(o instanceof RolePermissionRelationship)) return false;
        if (!super.equals(o)) return false;
        RolePermissionRelationship that = (RolePermissionRelationship) o;
        return Objects.equals(roleSid, that.roleSid) &&
                Objects.equals(permissionSid, that.permissionSid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), roleSid, permissionSid);
    }
}
