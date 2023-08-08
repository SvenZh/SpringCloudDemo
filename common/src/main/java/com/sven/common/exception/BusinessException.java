package com.sven.common.exception;

public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(IExceptionEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public BusinessException(IExceptionEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
