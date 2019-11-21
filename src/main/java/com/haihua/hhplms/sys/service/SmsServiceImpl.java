package com.haihua.hhplms.sys.service;

import com.github.qcloudsms.SmsSingleSenderResult;
import com.haihua.hhplms.client.sms.TencentSmsClient;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.JsonUtil;
import com.haihua.hhplms.sys.model.DynamicCodeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service("smsService")
public class SmsServiceImpl implements SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);

    private static final long TWO_MIN_IN_MINI_SECOND = 120 * 1000;

    @Autowired
    @Qualifier("tencentSmsClient")
    private TencentSmsClient tencentSmsClient;

    private ConcurrentHashMap<String, DynamicCodeHolder> dynamicCodeCache = new ConcurrentHashMap<>();

    private String generateDynamicCode() {
        final StringBuilder dynamicCode = new StringBuilder();
        final Random random = new Random();
        for (int i = 0; i < 6; i++) {
            dynamicCode.append(random.nextInt(10));
        }
        return dynamicCode.toString();
    }

    public void sendDynamicCode(final String mobile) {
        final DynamicCodeHolder dynamicCodeHolder = dynamicCodeCache.get(mobile);
        if (Objects.isNull(dynamicCodeHolder)) {
            final String dynamicCode = generateDynamicCode();
            final SmsSingleSenderResult result = tencentSmsClient.sendDynamicCode(mobile, dynamicCode);
            if (log.isInfoEnabled()) {
                log.info(String.format("发送验证码: %s到手机: %s, 发送结果: %s", dynamicCode, mobile, JsonUtil.toJson(result)));
            }

            dynamicCodeCache.put(mobile, new DynamicCodeHolder(dynamicCode, System.currentTimeMillis() + TWO_MIN_IN_MINI_SECOND));
            tencentSmsClient.sendDynamicCode(mobile, dynamicCode);
        } else {
            long expireTime = dynamicCodeHolder.getExpireTime();
            if (System.currentTimeMillis() > expireTime) {
                final String dynamicCode = generateDynamicCode();
                final SmsSingleSenderResult result = tencentSmsClient.sendDynamicCode(mobile, dynamicCode);
                dynamicCodeCache.put(mobile, new DynamicCodeHolder(generateDynamicCode(), System.currentTimeMillis() + TWO_MIN_IN_MINI_SECOND));
                if (log.isInfoEnabled()) {
                    log.info(String.format("发送验证码: %s到手机: %s, 发送结果: %s", dynamicCode, mobile, JsonUtil.toJson(result)));
                }
            } else {
                throw new ServiceException("每2分钟可以获取一次验证码, 请稍后再获取");
            }
        }
    }
}
