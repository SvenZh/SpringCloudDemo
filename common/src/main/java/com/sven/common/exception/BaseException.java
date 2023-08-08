package com.sven.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected IExceptionEnum responseEnum;

    protected Object[] args;

    public BaseException(IExceptionEnum responseEnum) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.responseEnum = new IExceptionEnum() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return msg;
            }
        };
    }

    public BaseException(IExceptionEnum responseEnum, Object[] args, String message) {
        super(message);
        this.responseEnum = responseEnum;
        this.args = args;
    }

    public BaseException(IExceptionEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.responseEnum = responseEnum;
        this.args = args;
    }

}
