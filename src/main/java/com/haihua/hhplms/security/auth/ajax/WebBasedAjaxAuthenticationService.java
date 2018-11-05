package com.haihua.hhplms.security.auth.ajax;

import com.haihua.hhplms.security.model.UserBasicInfo;
import com.haihua.hhplms.security.model.UserBasicInfo;


public interface WebBasedAjaxAuthenticationService extends AjaxAuthenticationService {
    UserBasicInfo loadUserBasicInfoByMobile(String mobile);
    UserBasicInfo fastLogin(String mobile, String dynamicCode);
}
