package com.haihua.hhplms.security.auth.ajax;

import com.haihua.hhplms.security.model.token.JwtToken;
import com.haihua.hhplms.security.model.token.JwtTokenFactory;
import com.haihua.hhplms.security.model.*;
import com.haihua.hhplms.security.model.token.JwtToken;
import com.haihua.hhplms.security.model.token.JwtTokenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AjaxAuthenticationResultHandler {
    @Autowired
    private JwtTokenFactory tokenFactory;

    public Map<String, Object> handleOnSuccess(String username, String userType) {
        JwtToken accessToken = tokenFactory.createAccessJwtToken(username, userType);
        JwtToken refreshToken = tokenFactory.createRefreshToken(username, userType);
        Map<String, Object> result = new HashMap<>();
        result.put("token", accessToken.getToken());
        result.put("refreshToken", refreshToken.getToken());
        return result;
    }
}
