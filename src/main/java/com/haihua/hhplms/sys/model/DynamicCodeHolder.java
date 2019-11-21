package com.haihua.hhplms.sys.model;

public class DynamicCodeHolder {
    private String dynamicCode;
    private Long expireTime;

    public DynamicCodeHolder(final String dynamicCode, final Long expireTime) {
        this.dynamicCode = dynamicCode;
        this.expireTime = expireTime;
    }

    public String getDynamicCode() {
        return dynamicCode;
    }

    public Long getExpireTime() {
        return expireTime;
    }
}
