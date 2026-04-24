package com.costflow.accounting.application.common;

public class ConflictException extends BusinessException {

    public ConflictException(final String message) {
        super(ErrorCode.CONFLICT, message);
    }
}
