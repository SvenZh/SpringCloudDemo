package com.sven.common.domain.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage<T> implements IBaseResponseMessage<T> {

    private T data;
    
    private int code;
    
    private ErrorDetails error;

    public ResponseMessage(T data, int code) {
        this.data = data;
        this.code = code;
    }

    public ResponseMessage(ErrorDetails error, int code) {
        this.code = code;
        this.error = error;
    }

    public ResponseMessage(ErrorDetails error) {
        this.code = error.getErrorCode();
        this.error = error;
    }

    public static <T> ResponseMessage<T> ok(T data) {
        return new ResponseMessage<>(data, 200);
    }
    
    @Override
    public T data() {
        return data;
    }

    @Override
    public boolean isSuccess() {
        return error == null;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public ErrorDetails errors() {
        return error;
    }
}
