package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;
import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.Objects;

public class Role extends BaseEntity {
    private String code;
    private String name;
    private Category category;
    private Type type;
    private Long companyInfoSid;
    private Status status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getCompanyInfoSid() {
        return companyInfoSid;
    }

    public void setCompanyInfoSid(Long companyInfoSid) {
        this.companyInfoSid = companyInfoSid;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return Objects.equals(code, role.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), code);
    }

    public enum Category implements BaseEnum {
        EMPLOYEE("employee", "雇员"),
        ACCOUNT("account", "账号");
        private String code;
        private String name;
        Category(String code, String name) {
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

    public enum Type implements BaseEnum {
        COMPANY_TEMP("company-temp", "企业模板"),
        PRE_ASSIGNED("pre-assigned", "预分配"),
        CREATED("created", "创建");
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
