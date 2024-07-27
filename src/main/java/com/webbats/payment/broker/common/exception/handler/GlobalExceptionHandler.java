package com.webbats.payment.broker.common.exception.handler;

import com.webbats.payment.broker.common.exception.DuplicateRecordException;
import com.webbats.payment.broker.common.exception.NotFoundException;
import com.webbats.payment.broker.common.exception.ServiceRuntimeException;
import com.webbats.payment.broker.common.exception.ValidationFailureException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ErrorMessage handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return getErrorMessage(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorMessage handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.error(ex.getMessage(), ex);
        return getErrorMessage(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(ValidationFailureException.class)
    public ErrorMessage handleDataIntegrityViolation(ValidationFailureException ex) {
        log.error(ex.getMessage(), ex);
        return getErrorMessage(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ErrorMessage handleDataIntegrityViolation(DuplicateRecordException ex) {
        log.error(ex.getMessage(), ex);
        return getErrorMessage(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorMessage handleGenericException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return getErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    @ExceptionHandler(ServiceRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGenericException(ServiceRuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return getErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorMessage handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        return getErrorMessage(HttpStatus.FORBIDDEN, "Resource not allowed");
    }

    private static ErrorMessage getErrorMessage(HttpStatus httpStatus, String errorMessage) {
        return new ErrorMessage(httpStatus, errorMessage);
    }

    private static ErrorMessage getErrorMessage(HttpStatus httpStatus, Exception e) {
        return new ErrorMessage(httpStatus, e.getMessage());
    }

    private static ErrorMessage getErrorMessage(Long errorCode, String errorMessage) {
        return new ErrorMessage(errorCode, errorMessage);
    }

    @Getter
    static class ErrorMessage {
        private final long errorCode;
        private final String errorMessage;

        public ErrorMessage(HttpStatus status, String message) {
            this.errorCode = status.value();
            this.errorMessage = message;
        }

        public ErrorMessage(long errorCode, String message) {
            this.errorCode = errorCode;
            this.errorMessage = message;
        }

    }
}
