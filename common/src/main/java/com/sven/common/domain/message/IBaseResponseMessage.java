package com.sven.common.domain.message;

public interface IBaseResponseMessage<T> {
    T data();

    boolean isSuccess();

    int code();

    ErrorDetails errors();
}
