package com.haihua.hhplms.security.exception;

import org.springframework.security.core.AuthenticationException;

public class UserMobileNotBindingException extends AuthenticationException {

    public UserMobileNotBindingException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserMobileNotBindingException(String msg) {
        super(msg);
    }
}
