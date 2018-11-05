package com.haihua.hhplms.security.auth.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haihua.hhplms.security.exception.JwtExpiredTokenException;
import com.haihua.hhplms.common.model.ErrorCode;
import com.haihua.hhplms.common.model.ErrorResponse;
import com.haihua.hhplms.security.exception.JwtExpiredTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger log = LoggerFactory.getLogger(AjaxAwareAuthenticationFailureHandler.class);
    private final ObjectMapper mapper;

    @Autowired
    public AjaxAwareAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        log.error(e.getMessage(), e);

        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (e instanceof BadCredentialsException) {
            mapper.writeValue(response.getWriter(), ErrorResponse.of("Invalid username or password", ErrorCode.BAD_CREDENTIAL, HttpStatus.UNAUTHORIZED));
        } else if (e instanceof JwtExpiredTokenException) {
            mapper.writeValue(response.getWriter(), ErrorResponse.of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
        } else if (e instanceof UsernameNotFoundException) {
            mapper.writeValue(response.getWriter(), ErrorResponse.of("User does not exist", ErrorCode.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED));
        } else if (e instanceof DisabledException) {
            mapper.writeValue(response.getWriter(), ErrorResponse.of("User is not in active status", ErrorCode.USER_DISABLED, HttpStatus.UNAUTHORIZED));
        } else {
            mapper.writeValue(response.getWriter(), ErrorResponse.of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        }
    }
}
