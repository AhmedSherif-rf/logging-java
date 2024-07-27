package com.webbats.payment.broker.common.response;

import org.springframework.http.HttpStatus;

public record RequestError(String message, HttpStatus httpCode, Long code) {
}
