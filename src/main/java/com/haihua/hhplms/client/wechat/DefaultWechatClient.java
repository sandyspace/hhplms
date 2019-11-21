package com.haihua.hhplms.client.wechat;

import com.haihua.hhplms.client.wechat.model.AccessTokenWrapper;
import com.haihua.hhplms.client.wechat.model.UserInfoWrapper;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component("wechatClient")
public class DefaultWechatClient implements WechatClient {

    private static final Logger log = LoggerFactory.getLogger(DefaultWechatClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wechat.oauth.accessToken.retrieve.url:}")
    private String wechatOauthAccessTokenRetrieveUrl;

    @Value("${wechat.user.info.retrieve.url:}")
    private String wechatUserInfoRetrieveUrl;

    //appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
    public AccessTokenWrapper retrieveAccessToken(final String appId, final String appSecret, final String code, final String grantType) {
        final String url = String.format("%s?appid=%s&secret=%s&code=%s&grant_type=%s", wechatOauthAccessTokenRetrieveUrl,
                appId, appSecret, code, grantType);

        if (log.isInfoEnabled()) {
            log.info(String.format("retrieveAccessToken -------> url: %s", url));
        }

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
        } catch (RestClientException e) {
            throw new ServiceException(-9999, "获取AccessToken失败");
        }

        if (log.isInfoEnabled()) {
            log.info(String.format("retrieveAccessToken ------> raw response: %s", JsonUtil.toJson(response)));
        }

        final AccessTokenWrapper accessTokenWrapper = JsonUtil.parse(response.getBody(), AccessTokenWrapper.class);
        if (Objects.isNull(accessTokenWrapper)) {
            throw new ServiceException(-9999, "获取AccessToken失败");
        }

        if (Objects.nonNull(accessTokenWrapper.getErrCode())) {
            throw new ServiceException(accessTokenWrapper.getErrCode(), accessTokenWrapper.getErrMsg());
        }

        return accessTokenWrapper;
    }

    //access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
    public UserInfoWrapper retrieveUserInfo(final String accessToken, final String openId, final String lang) {
        final String url = String.format("%s?access_token=%s&openid=%s&lang=%s", wechatUserInfoRetrieveUrl,
                accessToken, openId, lang);

        if (log.isInfoEnabled()) {
            log.info(String.format("retrieveUserInfo -------> url: %s", url));
        }

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<String>() {});
        } catch (RestClientException e) {
            throw new ServiceException(-9999, "获取用户信息失败");
        }

        if (log.isInfoEnabled()) {
            log.info(String.format("retrieveUserInfo ------> raw response: %s", JsonUtil.toJson(response)));
        }

        final UserInfoWrapper userInfoWrapper = JsonUtil.parse(response.getBody(), UserInfoWrapper.class);

        if (Objects.isNull(userInfoWrapper)) {
            throw new ServiceException(-9999, "获取用户信息失败");
        }

        if (Objects.nonNull(userInfoWrapper.getErrCode())) {
            throw new ServiceException(userInfoWrapper.getErrCode(), userInfoWrapper.getErrMsg());
        }

        return userInfoWrapper;
    }
}
