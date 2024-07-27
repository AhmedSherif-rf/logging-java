package com.webbats.payment.broker.common.exception;


import com.webbats.payment.broker.common.response.RequestError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ServiceRuntimeException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    private final String message;
    private final HttpStatus httpStatus;
    private Throwable cause;
    private Long errorCode;
    private String extraMessage;
    private ResponseEntity<Object> errorResponse;
    private Map<String, Object> extraAttributes;

    public ServiceRuntimeException(RequestError error) {
        this(error.message(), error.httpCode(), error.code());
    }

    public ServiceRuntimeException(RequestError error, Map<String, Object> extraAttributes) {
        this(error.message(), error.httpCode(), error.code());
        this.extraAttributes = extraAttributes;
    }

    public ServiceRuntimeException(String message, HttpStatus httpStatus, Long code) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.errorCode = code;
    }

    public ServiceRuntimeException(String message) {
        this.message = message;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ServiceRuntimeException(String message, HttpStatus httpStatus, Long code, ResponseEntity<Object> errorResponse, Throwable cause) {
        super();
        this.message = message;
        this.httpStatus = httpStatus;
        this.errorResponse = errorResponse;
        this.errorCode = code;
    }

    public Map<String, Object> getExtraAttributes() {
        return extraAttributes;
    }

    public ResponseEntity<Object> getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ResponseEntity<Object> errorResponse) {
        this.errorResponse = errorResponse;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Throwable getCause() {
        return cause;
    }

    public String toString() {
        return httpStatus.toString();
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

}
