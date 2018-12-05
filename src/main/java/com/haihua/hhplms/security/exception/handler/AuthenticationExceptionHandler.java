package com.haihua.hhplms.security.exception.handler;

import com.haihua.hhplms.common.model.ErrorCode;
import com.haihua.hhplms.common.model.ErrorResponse;
import com.haihua.hhplms.security.exception.JwtBadTokenException;
import com.haihua.hhplms.security.exception.JwtExpiredTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handlerServiceException(AuthenticationException e) {
        log.error(e.getMessage(), e);

        ErrorCode errorCode;
        if (e instanceof BadCredentialsException) {
            errorCode = ErrorCode.BAD_CREDENTIAL;
        } else if (e instanceof JwtExpiredTokenException) {
            errorCode = ErrorCode.JWT_TOKEN_EXPIRED;
        } else if (e instanceof JwtBadTokenException) {
            errorCode = ErrorCode.JWT_TOKEN_BAD;
        } else if (e instanceof UsernameNotFoundException) {
            errorCode = ErrorCode.USER_NOT_FOUND;
        } else if (e instanceof DisabledException) {
            errorCode = ErrorCode.USER_DISABLED;
        } else {
            errorCode = ErrorCode.AUTHENTICATION;
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getMessage(), errorCode, HttpStatus.UNAUTHORIZED));
    }
}
