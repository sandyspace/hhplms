package com.haihua.hhplms.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.haihua.hhplms.common.model.InstanceBuilder;

public class UserBasicInfo {
    @JsonIgnore
    private Long id;
    private String loginName;
    private String email;
    private String mobile;
    @JsonIgnore
    private String type;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String status;
    private String realName;
    private String nickName;
    private String title;
    private String gender;
    private String headImgUrl;
    @JsonIgnore
    private Long companyId;

    private UserBasicInfo(Builder userBasicInfoBuilder) {
        this.id = userBasicInfoBuilder.id;
        this.loginName = userBasicInfoBuilder.loginName;
        this.email = userBasicInfoBuilder.email;
        this.mobile = userBasicInfoBuilder.mobile;
        this.type = userBasicInfoBuilder.type;
        this.password = userBasicInfoBuilder.password;
        this.status = userBasicInfoBuilder.status;
        this.realName = userBasicInfoBuilder.realName;
        this.nickName = userBasicInfoBuilder.nickName;
        this.title = userBasicInfoBuilder.title;
        this.gender = userBasicInfoBuilder.gender;
        this.headImgUrl = userBasicInfoBuilder.headImgUrl;
        this.companyId = userBasicInfoBuilder.companyId;
    }

    public Long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getRealName() {
        return realName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getTitle() {
        return title;
    }

    public String getGender() {
        return gender;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String toString() {
        return "{loginName: " + loginName + ",email: " + email + ",mobile: " + mobile + ",realName: " + realName + ",nickName: " + nickName + ",title: " + title + ",gender: " + gender + "}";
    }

    public static class Builder implements InstanceBuilder<UserBasicInfo> {
        private Long id;
        private String loginName;
        private String email;
        private String mobile;
        private String type;
        private String password;
        private String status;
        private String realName;
        private String nickName;
        private String title;
        private String gender;
        private String headImgUrl;
        private Long companyId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder loginName(String loginName) {
            this.loginName = loginName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder realName(String realName) {
            this.realName = realName;
            return this;
        }

        public Builder nickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder headImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
            return this;
        }

        public Builder companyId(Long companyId) {
            this.companyId = companyId;
            return this;
        }

        @Override
        public UserBasicInfo build() {
            return new UserBasicInfo(this);
        }
    }
}
