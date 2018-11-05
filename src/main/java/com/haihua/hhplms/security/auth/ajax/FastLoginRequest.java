package com.haihua.hhplms.security.auth.ajax;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FastLoginRequest {
    private String mobile;
    private String dynamicCode;

    @JsonCreator
    public FastLoginRequest(@JsonProperty("mobile") String mobile, @JsonProperty("dynamicCode") String dynamicCode) {
        this.mobile = mobile;
        this.dynamicCode = dynamicCode;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDynamicCode() {
        return dynamicCode;
    }
}
