package com.haihua.hhplms.common.exception;

public class ServiceException extends RuntimeException {

    private Integer code;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(final Integer code, final String message) {
        this(message);
        this.code = code;
    }

    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServiceException(final Integer code, final String message, final Throwable cause) {
        this(message, cause);
        this.code = code;
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return code;
    }
}
