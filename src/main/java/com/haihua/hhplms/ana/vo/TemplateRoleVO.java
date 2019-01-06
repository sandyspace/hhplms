package com.haihua.hhplms.ana.vo;

import com.haihua.hhplms.ana.entity.TemplateRole;

public class TemplateRoleVO {
    private Long id;
    private String code;
    private String name;
    private String memo;
    private String type;
    private String status;

    public TemplateRoleVO(TemplateRole templateRole) {
        this.id = templateRole.getSid();
        this.code = templateRole.getCode();
        this.name = templateRole.getName();
        this.memo = templateRole.getMemo();
        this.type = templateRole.getType().getCode();
        this.status = templateRole.getStatus().getCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
