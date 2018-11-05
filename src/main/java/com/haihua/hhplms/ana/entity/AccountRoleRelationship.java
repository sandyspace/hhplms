package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;

import java.util.Objects;

public class AccountRoleRelationship extends BaseEntity {
    private Long accountSid;
    private Long roleSid;

    public Long getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(Long accountSid) {
        this.accountSid = accountSid;
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
        if (!(o instanceof AccountRoleRelationship)) return false;
        if (!super.equals(o)) return false;
        AccountRoleRelationship that = (AccountRoleRelationship) o;
        return Objects.equals(accountSid, that.accountSid) &&
                Objects.equals(roleSid, that.roleSid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), accountSid, roleSid);
    }
}
