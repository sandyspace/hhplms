package com.haihua.hhplms.security.endpoint;

import com.haihua.hhplms.security.auth.ajax.WebBasedAjaxAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class WechatCallbackEndPoint {

    @Autowired
    @Qualifier("accountService")
    private WebBasedAjaxAuthenticationService webBasedAjaxAuthenticationService;

    @RequestMapping(method = RequestMethod.GET, path = "/api/auth/account/wechatAuthCallback")
    public String wechatAuthCallback(@RequestParam(value = "code") final String code,
                                     @RequestParam(value = "state", required = false) final String state) {
        final String wechatLoginRedirectUrl = webBasedAjaxAuthenticationService.wechatAuthCallback(code);
        String encodedWechatLoginRedirectUrl = null;
        try {
            encodedWechatLoginRedirectUrl = URLEncoder.encode(wechatLoginRedirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedWechatLoginRedirectUrl = "";
        }
        return String.format("redirect:%s", encodedWechatLoginRedirectUrl);
    }
}
