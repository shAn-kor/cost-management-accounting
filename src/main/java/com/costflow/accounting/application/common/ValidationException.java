package com.costflow.accounting.application.common;

public class ValidationException extends BusinessException {

    public ValidationException(final String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }
}
