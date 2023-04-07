package com.sven.service.conf;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;

@RestControllerAdvice
public class ErrorHandlerConfiguration {

    @ExceptionHandler(value = Exception.class)
    public IBaseResponseMessage<?> test(Exception ex) {
        ErrorDetails error = new ErrorDetails(500, ex.getMessage());

        ResponseMessage<String> response = new ResponseMessage<>(null, 500, error);
        return response;
    }
}
