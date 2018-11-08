package com.haihua.hhplms.ana.vo;

import com.haihua.hhplms.ana.entity.CompanyInfo;

public class CompanyInfoVO {
    private Long id;
    private String code;
    private String name;
    private String type;
    private String address;
    private String contactName;
    private String contactMobile;
    private String status;

    public CompanyInfoVO(CompanyInfo companyInfo) {
        this.id = companyInfo.getSid();
        this.code = companyInfo.getCode();
        this.name = companyInfo.getName();
        this.type = companyInfo.getType().getCode();
        this.address = companyInfo.getAddress();
        this.contactName = companyInfo.getContactName();
        this.contactMobile = companyInfo.getContactMobile();
        this.status = companyInfo.getStatus().getCode();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
