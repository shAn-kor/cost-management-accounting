package com.costflow.accounting.interfaces.api.common;

public record ApiErrorResponse(
    String code,
    String message
) {
}
