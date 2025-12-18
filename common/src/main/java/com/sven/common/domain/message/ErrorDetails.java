package com.sven.common.domain.message;

import java.io.Serializable;

import com.sven.common.exception.BusinessExceptionEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails implements Serializable {
    private int errorCode;
    private String errorMessage;

    public ErrorDetails(BusinessExceptionEnum ex) {
        this.errorCode = ex.getCode();
        this.errorMessage = ex.getMessage();
    }
}
