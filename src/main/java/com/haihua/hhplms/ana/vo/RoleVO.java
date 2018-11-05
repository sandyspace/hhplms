package com.haihua.hhplms.ana.vo;


import com.haihua.hhplms.ana.entity.Role;

public class RoleVO {
    private Long id;
    private String code;
    private String name;
    private String category;
    private String type;
    private Long companyId;
    private String status;
    private Integer versionNum;

    public RoleVO(Role role) {
        this.id = role.getSid();
        this.code = role.getCode();
        this.name = role.getName();
        this.category = role.getCategory().getCode();
        this.type = role.getType().getCode();
        this.companyId = role.getCompanyInfoSid();
        this.status = role.getStatus().getCode();
        this.versionNum = role.getVersionNum();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }
}
