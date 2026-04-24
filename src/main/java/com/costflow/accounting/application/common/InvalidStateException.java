package com.costflow.accounting.application.common;

public class InvalidStateException extends BusinessException {

    public InvalidStateException(final String message) {
        super(ErrorCode.INVALID_STATE, message);
    }
}
