package com.haihua.hhplms.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haihua.hhplms.common.model.InstanceBuilder;

public class GrantedRole {
    @JsonIgnore
    private Long id;
    private String code;
    private String name;

    private GrantedRole(Builder grantedRoleBuilder) {
        this.id = grantedRoleBuilder.id;
        this.code = grantedRoleBuilder.code;
        this.name = grantedRoleBuilder.name;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static class Builder implements InstanceBuilder<GrantedRole> {
        private Long id;
        private String code;
        private String name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public GrantedRole build() {
            return new GrantedRole(this);
        }
    }
}
