package com.haihua.hhplms.client.sms;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.haihua.hhplms.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("tencentSmsClient")
public class DefaultTencentSmsClient implements TencentSmsClient {

    @Value("${sms.tencent.app.id}")
    private Integer appId;

    @Value("${sms.tencent.app.key}")
    private String appKey;

    @Value("${sms.tencent.template.dynamic.code.id}")
    private Integer dynamicCodeTemplateId;

    @Value("${sms.tencent.sign}")
    private String sign;

    public SmsSingleSenderResult sendDynamicCode(final String phoneNum, final String dynamicCode) {
        final SmsSingleSender smsSingleSender = new SmsSingleSender(appId, appKey);
        SmsSingleSenderResult smsSingleSenderResult = null;
        try {
            smsSingleSenderResult = smsSingleSender.sendWithParam("86", phoneNum, dynamicCodeTemplateId, new String [] {dynamicCode, "2"}, sign, "", "");
        } catch (Exception e) {
            throw new ServiceException("发送验证码失败, 请稍后再试", e);
        }
        return smsSingleSenderResult;
    }
}
