package com.aramdev.delivery.exception;

import lombok.Getter;

@Getter
public class BusinessValidationException extends RuntimeException {

    private final String errorCode;

    public BusinessValidationException(Enum<?> errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode.name();
    }

    public BusinessValidationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessValidationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public BusinessValidationException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }
}