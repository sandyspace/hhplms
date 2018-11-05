package com.haihua.hhplms.ana.entity;

import com.haihua.hhplms.common.entity.BaseEntity;
import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.List;
import java.util.Objects;

public class Employee extends BaseEntity {
    private String loginName;
    private String realName;
    private String password;
    private String email;
    private String mobile;
    private String tel;
    private Gender gender;
    private String idCard;
    private String title;
    private String headImgUrl;
    private Status status;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
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
        if (!(o instanceof Employee)) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(loginName, employee.loginName) &&
                Objects.equals(email, employee.email) &&
                Objects.equals(mobile, employee.mobile);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), loginName, email, mobile);
    }

    public enum Status implements BaseEnum {
        ACTIVE("active", "活跃"),
        DISABLED("disabled", "禁用"),
        LEAVE("leave", "休假"),
        DIMISSION("dimission", "离职");

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
