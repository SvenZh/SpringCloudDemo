package com.sven.common.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sven.common.domain.message.ErrorDetails;
import com.sven.common.domain.message.IBaseResponseMessage;
import com.sven.common.domain.message.ResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public IBaseResponseMessage<?> defalut(Exception ex) {
        ErrorDetails error = new ErrorDetails(500, ex.getMessage());

        ResponseMessage<String> response = new ResponseMessage<>(error);
        return response;
    }

    @ExceptionHandler(value = BaseException.class)
    public IBaseResponseMessage<?> resolvebaseException(BaseException ex) {
        ErrorDetails error = new ErrorDetails(ex.getResponseEnum().getCode(), ex.getMessage());

        ResponseMessage<String> response = new ResponseMessage<>(error);
        return response;
    }

    @ExceptionHandler({ BindException.class, MethodArgumentNotValidException.class,
            ConstraintViolationException.class })
    public IBaseResponseMessage<?> resolveMethodArgumentNotValidException(Exception ex) {

        ErrorDetails error = new ErrorDetails(BusinessExceptionEnum.valid_exception.getCode(), handlerNotValidException(ex));
        ResponseMessage<String> response = new ResponseMessage<>(error);

        return response;
    }

    private String handlerNotValidException(Exception e) {
        BindingResult bindingResult = null;
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        if (e instanceof BindException) {
            BindException exception = (BindException) e;
            bindingResult = exception.getBindingResult();
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            bindingResult = exception.getBindingResult();
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exception = (ConstraintViolationException) e;
            constraintViolations = exception.getConstraintViolations();
        }
        Map<String, Object> maps;
        if (Objects.nonNull(bindingResult) && bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            maps = new HashMap<>(fieldErrors.size());
            fieldErrors.forEach(error -> maps.put(error.getField(), error.getDefaultMessage()));
        } else if (!CollectionUtils.isEmpty(constraintViolations)) {
            maps = new HashMap<>(constraintViolations.size());
            constraintViolations.forEach(error -> {
                if (error instanceof ConstraintViolationImpl) {
                    ConstraintViolationImpl<?> constraintViolation = (ConstraintViolationImpl<?>) error;
                    maps.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                }
            });
        } else {
            maps = Collections.emptyMap();
        }

        StringJoiner response = new StringJoiner(",");
        maps.forEach((k, v) -> {
            response.add(k + v);
        });

        return response.toString();
    }
}
