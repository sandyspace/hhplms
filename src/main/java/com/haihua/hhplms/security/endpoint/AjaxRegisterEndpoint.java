package com.haihua.hhplms.security.endpoint;

import com.haihua.hhplms.security.auth.ajax.RegisterRequest;
import com.haihua.hhplms.security.auth.ajax.WebBasedAjaxAuthenticationService;
import com.haihua.hhplms.security.model.UserBasicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AjaxRegisterEndpoint {
    @Autowired
    @Qualifier("accountService")
    private WebBasedAjaxAuthenticationService webBasedAjaxAuthenticationService;

    @PostMapping(path = "/api/auth/account/register", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserBasicInfo> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(webBasedAjaxAuthenticationService.register(registerRequest));
    }
}
