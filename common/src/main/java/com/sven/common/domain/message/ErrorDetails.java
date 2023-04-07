package com.sven.common.domain.message;

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

    public ErrorDetails(SystemEvent systemEvent) {
        this.errorCode = systemEvent.getErrorCode();
        this.errorMessage = systemEvent.getErrorMessage();
    }
}
