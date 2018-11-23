package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;
import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.Objects;

public class Account extends BaseEntity {
    private String loginName;
    private String nickName;
    private String realName;
    private String mobile;
    private String email;
    private String password;
    private Gender gender;
    private String headImgUrl;
    private Type type;
    private Status status;
    private Long companyInfoSid;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getCompanyInfoSid() {
        return companyInfoSid;
    }

    public void setCompanyInfoSid(Long companyInfoSid) {
        this.companyInfoSid = companyInfoSid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        if (!super.equals(o)) return false;
        Account account = (Account) o;
        return Objects.equals(mobile, account.mobile);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), mobile);
    }

    public enum Type implements BaseEnum {
        //member: 普通用户, company: 企业
        MEMBER("member", "普通用户"),
        COMPANY("company", "企业用户");

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
        ACTIVE("active", "活跃"),
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
