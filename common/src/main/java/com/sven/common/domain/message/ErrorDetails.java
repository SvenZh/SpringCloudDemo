package com.sven.common.domain.message;

import com.sven.common.exception.BusinessExceptionEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private int errorCode;
    private String errorMessage;

    public ErrorDetails(BusinessExceptionEnum ex) {
        this.errorCode = ex.getCode();
        this.errorMessage = ex.getMessage();
    }
}
