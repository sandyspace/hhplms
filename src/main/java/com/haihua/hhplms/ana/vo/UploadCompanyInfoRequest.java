package com.haihua.hhplms.ana.vo;

public class UploadCompanyInfoRequest {
    private String code;
    private String name;
    private String type;
    private String address;
    private String contactName;
    private String contactPhone;
    private String companyOwnerName;
    private String companyOwnerSex;

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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getCompanyOwnerName() {
        return companyOwnerName;
    }

    public void setCompanyOwnerName(String companyOwnerName) {
        this.companyOwnerName = companyOwnerName;
    }

    public String getCompanyOwnerSex() {
        return companyOwnerSex;
    }

    public void setCompanyOwnerSex(String companyOwnerSex) {
        this.companyOwnerSex = companyOwnerSex;
    }
}
