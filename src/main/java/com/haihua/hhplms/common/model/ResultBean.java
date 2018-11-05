package com.haihua.hhplms.common.model;

import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.constant.GlobalConstant;

public class ResultBean {
    public static class Success<T> {
        private int status = GlobalConstant.STATUS_SUCCESS;
        private T content;
        private String msg;

        private Success(T content, String msg) {
            this.content = content;
            this.msg = msg;

        }

        public int getStatus() {
            return status;
        }

        public T getContent() {
            return content;
        }

        public String getMsg() {
            return msg;
        }

        public static <T> Success of(T content, String msg) {
            return new Success(content, msg);
        }
    }

    public static class Failure {
        private int status = GlobalConstant.STATUS_FAILURE;
        private int errorCode;
        private String errorMsg;

        private Failure(int errorCode, String errorMsg) {
            this.errorCode = errorCode;
            this.errorMsg = errorMsg;
        }

        public int getStatus() {
            return status;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public static Failure of(int errorCode, String errorMsg) {
            return new Failure(errorCode, errorMsg);
        }
    }
}
