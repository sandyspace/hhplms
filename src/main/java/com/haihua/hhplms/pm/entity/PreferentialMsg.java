package com.haihua.hhplms.pm.entity;

import com.haihua.hhplms.common.entity.BaseEntity;
import com.haihua.hhplms.common.entity.BaseEnum;

import java.util.Objects;

public class PreferentialMsg extends BaseEntity {
    private String title;
    private String content;
    private String imgUrl;
    private Status status;
    private Long companyInfoSid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
        if (!(o instanceof PreferentialMsg)) return false;
        if (!super.equals(o)) return false;
        PreferentialMsg that = (PreferentialMsg) o;
        return Objects.equals(getSid(), that.getSid());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getSid());
    }

    public enum Status implements BaseEnum {
        DRAFT("draft", "草稿"),
        RELEASE("release", "发布");

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
