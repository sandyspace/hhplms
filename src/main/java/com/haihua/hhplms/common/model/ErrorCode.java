package com.haihua.hhplms.common.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration of REST Error types.
 */
public enum ErrorCode {
    GLOBAL(2),
    AUTHENTICATION(10),
    BAD_CREDENTIAL(99),
    JWT_TOKEN_EXPIRED(11),
    USER_NOT_FOUND(12),
    USER_DISABLED(20);

    private int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @JsonValue
    public int getErrorCode() {
        return errorCode;
    }
}
