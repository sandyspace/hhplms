package com.haihua.hhplms.sys.service;

import com.github.qcloudsms.SmsSingleSenderResult;
import com.haihua.hhplms.client.sms.TencentSmsClient;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.DateUtil;
import com.haihua.hhplms.common.utils.JsonUtil;
import com.haihua.hhplms.sys.model.DynamicCodeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Override
    public void verifyDynamicCode(final String mobile, final String dynamicCode) {
        final DynamicCodeHolder dynamicCodeHolder = dynamicCodeCache.get(mobile);
        if (Objects.isNull(dynamicCodeHolder)) {
            throw new ServiceException("无效的验证码");
        }
        if (!Objects.equals(dynamicCodeHolder.getDynamicCode(), dynamicCode)) {
            throw new ServiceException("验证码错误");
        }
        final long currentTime = System.currentTimeMillis();
        final long expireTime = dynamicCodeHolder.getExpireTime();
        if (log.isInfoEnabled()) {
            log.info(String.format("校验验证码 ==> 验证验证码是否过期 currentTime: %s, expireTime: %s",
                    DateUtil.format(new Date(currentTime), "yyyy-MM-dd HH:mm:ss"),
                    DateUtil.format(new Date(expireTime), "yyyy-MM-dd HH:mm:ss")));
        }
        if (currentTime > expireTime) {
            throw new ServiceException("验证码已过期");
        }
        dynamicCodeCache.remove(mobile);
    }

    private void doSendDynamicCode(final String mobile) {
        final String dynamicCode = generateDynamicCode();
        final SmsSingleSenderResult result = tencentSmsClient.sendDynamicCode(mobile, dynamicCode);
        if (log.isInfoEnabled()) {
            log.info(String.format("发送验证码: %s到手机: %s, 发送结果: %s", dynamicCode, mobile, JsonUtil.toJson(result)));
        }
        final long currentTime = System.currentTimeMillis();
        final DynamicCodeHolder dynamicCodeHolder = new DynamicCodeHolder(dynamicCode, currentTime + TWO_MIN_IN_MINI_SECOND);
        dynamicCodeCache.put(mobile, dynamicCodeHolder);
    }

    @Override
    public void sendDynamicCode(final String mobile) {
        if (!dynamicCodeCache.containsKey(mobile)) {
            doSendDynamicCode(mobile);
        } else {
            final DynamicCodeHolder dynamicCodeHolder = dynamicCodeCache.get(mobile);
            final long expireTime = dynamicCodeHolder.getExpireTime();
            final long currentTime = System.currentTimeMillis();
            if (log.isInfoEnabled()) {
                log.info(String.format("发送验证码 ==> 验证验证码是否过期 currentTime: %s, expireTime: %s",
                        DateUtil.format(new Date(currentTime), "yyyy-MM-dd HH:mm:ss"),
                        DateUtil.format(new Date(expireTime), "yyyy-MM-dd HH:mm:ss")));
            }
            if (currentTime <= expireTime) {
                throw new ServiceException("每2分钟可以获取一次验证码, 请稍后再获取");
            }
            doSendDynamicCode(mobile);
        }
    }
}
