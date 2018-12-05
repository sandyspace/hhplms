package com.haihua.hhplms.security.exception;

import com.haihua.hhplms.security.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;

public class JwtInvalidatedTokenException extends AuthenticationException {

    private JwtToken token;

    public JwtInvalidatedTokenException(JwtToken token, String msg) {
        super(msg);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
