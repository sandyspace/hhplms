package com.haihua.hhplms.client.sms;

import com.github.qcloudsms.SmsSingleSenderResult;

public interface TencentSmsClient {
    SmsSingleSenderResult sendDynamicCode(final String phoneNum, final String dynamicCode);
}
