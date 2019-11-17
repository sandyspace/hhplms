package com.haihua.hhplms.client.wechat;

import com.haihua.hhplms.client.wechat.model.AccessTokenWrapper;
import com.haihua.hhplms.client.wechat.model.UserInfoWrapper;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component("wechatClient")
public class DefaultWechatClient implements WechatClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${wechat.oauth.accessToken.retrieve.url:}")
    private String wechatOauthAccessTokenRetrieveUrl;

    @Value("${wechat.user.info.retrieve.url}")
    private String wechatUserInfoRetrieveUrl;

    //appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
    public AccessTokenWrapper retrieveAccessToken(final String appId, final String appSecret, final String code, final String grantType) {
        final String url = String.format("%s?appid=%s&secret=%s&code=%s&grant_type=%s", wechatOauthAccessTokenRetrieveUrl,
                appId, appSecret, code, grantType);

        ResponseEntity<AccessTokenWrapper> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<AccessTokenWrapper>() {});
        } catch (RestClientException e) {
            throw new ServiceException("获取AccessToken失败");
        }

        final HttpStatus.Series series = HttpStatus.Series.valueOf(response.getStatusCode());
        if (HttpStatus.Series.CLIENT_ERROR == series || HttpStatus.Series.SERVER_ERROR == series) {
            if (Objects.nonNull(response.getBody())) {
                throw new ServiceException(response.getBody().getErrMsg());
            } else {
                throw new ServiceException(response.getStatusCode().getReasonPhrase());
            }
        }

        final AccessTokenWrapper accessTokenWrapper = response.getBody();
        if (Objects.isNull(accessTokenWrapper)) {
            throw new ServiceException("获取AccessToken失败");
        }
        if (StringUtils.isNotBlank(accessTokenWrapper.getErrCode())) {
            throw new ServiceException(accessTokenWrapper.getErrMsg());
        }
        return accessTokenWrapper;
    }

    //access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
    public UserInfoWrapper retrieveUserInfo(final String accessToken, final String openId, final String lang) {
        final String url = String.format("%s?access_token=%s&openid=%s&lang=%s", wechatUserInfoRetrieveUrl,
                accessToken, openId, lang);

        ResponseEntity<UserInfoWrapper> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<UserInfoWrapper>() {});
        } catch (RestClientException e) {
            throw new ServiceException("获取用户信息失败");
        }

        final HttpStatus.Series series = HttpStatus.Series.valueOf(response.getStatusCode());
        if (HttpStatus.Series.CLIENT_ERROR == series || HttpStatus.Series.SERVER_ERROR == series) {
            if (Objects.nonNull(response.getBody())) {
                throw new ServiceException(response.getBody().getErrMsg());
            } else {
                throw new ServiceException(response.getStatusCode().getReasonPhrase());
            }
        }

        final UserInfoWrapper userInfoWrapper = response.getBody();
        if (Objects.isNull(userInfoWrapper)) {
            throw new ServiceException("获取用户信息失败");
        }
        if (StringUtils.isNotBlank(userInfoWrapper.getErrCode())) {
            throw new ServiceException(userInfoWrapper.getErrMsg());
        }
        return userInfoWrapper;
    }
}
