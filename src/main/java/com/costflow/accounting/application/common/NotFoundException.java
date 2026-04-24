package com.costflow.accounting.application.common;

public class NotFoundException extends BusinessException {

    public NotFoundException(final String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
