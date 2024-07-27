package com.webbats.payment.broker.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RoleAssociatedException extends RuntimeException {

    public RoleAssociatedException(String message) {
        super(message);
    }
}
