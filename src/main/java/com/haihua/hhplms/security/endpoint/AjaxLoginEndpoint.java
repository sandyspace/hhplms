package com.haihua.hhplms.security.endpoint;

import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.security.auth.ajax.*;
import com.haihua.hhplms.security.model.UserBasicInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AjaxLoginEndpoint {

    @Autowired
    @Qualifier("employeeService")
    private AjaxAuthenticationService ajaxAuthenticationService;

    @Autowired
    @Qualifier("accountService")
    private WebBasedAjaxAuthenticationService webBasedAjaxAuthenticationService;

    @Autowired
    private AjaxAuthenticationResultHandler ajaxAuthenticationResultHandler;

    @PostMapping(path = "/api/auth/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("用户名密码不能为空");
        }
        UserBasicInfo basicInfo = ajaxAuthenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(ajaxAuthenticationResultHandler.handleOnSuccess(basicInfo.getLoginName(), basicInfo.getType()));
    }

    @PostMapping(path = "/api/auth/account/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, Object>> accountLogin(@RequestBody LoginRequest loginRequest) {
        if (StringUtils.isBlank(loginRequest.getUsername()) || StringUtils.isBlank(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("用户名密码不能为空");
        }
        UserBasicInfo basicInfo = webBasedAjaxAuthenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(ajaxAuthenticationResultHandler.handleOnSuccess(basicInfo.getLoginName(), basicInfo.getType()));
    }

    @PostMapping(path = "/api/auth/account/wechatLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, Object>> wechatLogin(@RequestBody final WechatLoginRequest wechatLoginRequest) {
        if (StringUtils.isBlank(wechatLoginRequest.getOpenId())) {
            throw new AuthenticationServiceException("OpenId不能为空");
        }
        final UserBasicInfo basicInfo = webBasedAjaxAuthenticationService.wechatLogin(wechatLoginRequest.getOpenId());
        return ResponseEntity.status(HttpStatus.OK).body(ajaxAuthenticationResultHandler.handleOnSuccess(basicInfo.getLoginName(), basicInfo.getType()));
    }

    @PostMapping(path = "/api/auth/account/fastLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, Object>> fastLogin(@RequestBody FastLoginRequest fastLoginRequest) {
        if (StringUtils.isBlank(fastLoginRequest.getMobile()) || StringUtils.isBlank(fastLoginRequest.getDynamicCode())) {
            throw new AuthenticationServiceException("手机号验证码不能为空");
        }
        //check dynamic code

        UserBasicInfo basicInfo = webBasedAjaxAuthenticationService.loadUserBasicInfoByMobile(fastLoginRequest.getMobile());
        return ResponseEntity.status(HttpStatus.OK).body(ajaxAuthenticationResultHandler.handleOnSuccess(fastLoginRequest.getMobile(), basicInfo.getType()));
    }

    @PostMapping(path = "/api/auth/account/wechat-mobile/binding", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ResultBean.Success<String>> bindingMobileWithWechat(@RequestBody final WechatMobileBindingRequest wechatMobileBindingRequest) {
        webBasedAjaxAuthenticationService.bindingMobileWithWechat(wechatMobileBindingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ResultBean.Success.of("", "绑定成功"));
    }

    @PostMapping(path = "/api/auth/account/pwd/setting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ResultBean.Success<String>> settingPassword(@RequestBody final PwdSettingRequest pwdSettingRequest) {
        webBasedAjaxAuthenticationService.settingPassword(pwdSettingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ResultBean.Success.of("", "密码设置成功"));
    }
}
