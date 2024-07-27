package com.webbats.payment.broker.common.exception;

import com.webbats.payment.broker.common.response.RequestError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateRecordException extends RuntimeException {

    private final String errorMessage;
    private final long errorCode;

    public DuplicateRecordException(RequestError error) {
        super(error.message());
        this.errorMessage = error.message();
        this.errorCode = error.code();
    }

    public DuplicateRecordException(Long errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public DuplicateRecordException(String errorMessage) {
        super(errorMessage);
        this.errorCode = HttpStatus.CONFLICT.value();
        this.errorMessage = errorMessage;
    }
}