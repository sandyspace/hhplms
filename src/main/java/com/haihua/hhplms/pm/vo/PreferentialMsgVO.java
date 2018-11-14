package com.haihua.hhplms.pm.vo;

import com.haihua.hhplms.pm.entity.PreferentialMsg;

import java.util.Objects;

public class PreferentialMsgVO {
    private Long id;
    private String title;
    private String content;
    private String imgUrl;
    private String status;
    private Long companyId;
    private Long createdTime;
    private Long updatedTime;

    public PreferentialMsgVO(PreferentialMsg preferentialMsg) {
        this.id = preferentialMsg.getSid();
        this.title = preferentialMsg.getTitle();
        this.content = preferentialMsg.getContent();
        this.imgUrl = preferentialMsg.getImgUrl();
        this.status = preferentialMsg.getStatus().getCode();
        this.companyId = preferentialMsg.getCompanyInfoSid();
        this.createdTime = Objects.isNull(preferentialMsg.getCreatedTime()) ? null : preferentialMsg.getCreatedTime().getTime();
        this.updatedTime = Objects.isNull(preferentialMsg.getUpdatedTime()) ? null : preferentialMsg.getUpdatedTime().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
