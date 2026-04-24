package com.costflow.accounting.interfaces.api.common;

import com.costflow.accounting.application.common.BusinessException;
import com.costflow.accounting.application.common.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(final BusinessException exception) {
        return ResponseEntity.status(statusOf(exception.getErrorCode()))
            .body(new ApiErrorResponse(exception.getErrorCode().name(), exception.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(final DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(ErrorCode.CONFLICT.name(), "이미 존재하는 데이터입니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(final MethodArgumentNotValidException exception) {
        final String message = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .orElse("요청 값이 올바르지 않습니다.");
        return ResponseEntity.badRequest().body(new ApiErrorResponse(ErrorCode.VALIDATION_ERROR.name(), message));
    }

    HttpStatus statusOf(final ErrorCode errorCode) {
        return switch (errorCode) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT, INVALID_STATE -> HttpStatus.CONFLICT;
            case VALIDATION_ERROR -> HttpStatus.UNPROCESSABLE_ENTITY;
        };
    }
}
