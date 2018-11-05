package com.haihua.hhplms.security.exception.handler;

import com.haihua.hhplms.common.model.ErrorCode;
import com.haihua.hhplms.common.model.ErrorResponse;
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

        ErrorResponse errorResponse;
        if (e instanceof BadCredentialsException) {
            errorResponse = ErrorResponse.of("Invalid username or password", ErrorCode.BAD_CREDENTIAL, HttpStatus.UNAUTHORIZED);
        } else if (e instanceof JwtExpiredTokenException) {
            errorResponse = ErrorResponse.of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        } else if (e instanceof UsernameNotFoundException) {
            errorResponse = ErrorResponse.of("User does not exist", ErrorCode.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        } else if (e instanceof DisabledException) {
            errorResponse = ErrorResponse.of("User is not in active status", ErrorCode.USER_DISABLED, HttpStatus.UNAUTHORIZED);
        }  else {
            errorResponse = ErrorResponse.of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
