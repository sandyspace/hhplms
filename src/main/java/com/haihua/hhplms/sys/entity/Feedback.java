package com.haihua.hhplms.sys.entity;

import com.haihua.hhplms.common.entity.BaseEntity;

import java.util.Objects;

public class Feedback extends BaseEntity {
    private String title;
    private String content;
    private String contact;
    private String contactMobile;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        if (!super.equals(o)) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(getSid(), feedback.getSid());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getSid());
    }
}
