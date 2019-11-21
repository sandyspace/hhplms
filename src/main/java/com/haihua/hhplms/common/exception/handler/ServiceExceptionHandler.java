package com.haihua.hhplms.common.exception.handler;

import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ServiceExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ServiceExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResultBean.Failure> handlerServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResultBean.Failure.of(Objects.isNull(e.getCode()) ? HttpStatus.INTERNAL_SERVER_ERROR.value() : e.getCode(), e.getMessage()));
    }
}