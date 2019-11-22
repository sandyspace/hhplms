package com.haihua.hhplms.sys.service;

public interface SmsService {

    void verifyDynamicCode(final String mobile, final String dynamicCode);

    void sendDynamicCode(final String mobile);
}
