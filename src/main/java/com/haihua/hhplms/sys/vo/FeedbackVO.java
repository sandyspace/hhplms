package com.haihua.hhplms.sys.vo;

import com.haihua.hhplms.sys.entity.Feedback;

import java.util.Objects;

public class FeedbackVO {
    private Long id;
    private String title;
    private String content;
    private String contact;
    private String contactMobile;
    private Long createdTime;

    public FeedbackVO(Feedback feedback) {
        this.id = feedback.getSid();
        this.title = feedback.getTitle();
        this.content = feedback.getContent();
        this.contact = feedback.getContact();
        this.contactMobile = feedback.getContactMobile();
        this.createdTime = Objects.isNull(feedback.getCreatedTime()) ? null : feedback.getCreatedTime().getTime();
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
}
