package com.haihua.hhplms.client.wechat;

import com.haihua.hhplms.client.wechat.model.AccessTokenWrapper;
import com.haihua.hhplms.client.wechat.model.UserInfoWrapper;

public interface WechatClient {

    AccessTokenWrapper retrieveAccessToken(final String appId, final String appSecret, final String code, final String grantType);

    UserInfoWrapper retrieveUserInfo(final String accessToken, final String openId, final String lang);
}
