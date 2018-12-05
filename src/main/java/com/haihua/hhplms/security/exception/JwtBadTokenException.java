package com.haihua.hhplms.security.exception;

import com.haihua.hhplms.security.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;

public class JwtBadTokenException extends AuthenticationException {

    private JwtToken token;

    public JwtBadTokenException(JwtToken token, String msg) {
        super(msg);
        this.token = token;
    }

    public JwtBadTokenException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
