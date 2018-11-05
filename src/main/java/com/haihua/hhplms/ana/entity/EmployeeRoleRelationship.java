package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;

import java.util.Objects;

public class EmployeeRoleRelationship extends BaseEntity {
    private Long employeeSid;
    private Long roleSid;

    public Long getEmployeeSid() {
        return employeeSid;
    }

    public void setEmployeeSid(Long employeeSid) {
        this.employeeSid = employeeSid;
    }

    public Long getRoleSid() {
        return roleSid;
    }

    public void setRoleSid(Long roleSid) {
        this.roleSid = roleSid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeRoleRelationship)) return false;
        if (!super.equals(o)) return false;
        EmployeeRoleRelationship that = (EmployeeRoleRelationship) o;
        return Objects.equals(employeeSid, that.employeeSid) &&
                Objects.equals(roleSid, that.roleSid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), employeeSid, roleSid);
    }
}
