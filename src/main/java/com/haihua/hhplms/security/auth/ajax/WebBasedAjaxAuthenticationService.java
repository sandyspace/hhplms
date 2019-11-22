package com.haihua.hhplms.security.auth.ajax;

import com.haihua.hhplms.security.model.UserBasicInfo;
import com.haihua.hhplms.security.model.UserBasicInfo;


public interface WebBasedAjaxAuthenticationService extends AjaxAuthenticationService {
    UserBasicInfo loadUserBasicInfoByMobile(String mobile);
    UserBasicInfo loadUserBasicInfoByOpenId(final String openId);
    UserBasicInfo wechatLogin(final String openId);
    String wechatAuthCallback(String code);
    void bindingMobileWithWechat(final WechatMobileBindingRequest wechatMobileBindingRequest);
    void settingPassword(final PwdSettingRequest pwdSettingRequest);
    UserBasicInfo fastLogin(String mobile, String dynamicCode);
}
