package com.webbats.payment.broker.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private Long code;
    private String errorMessage;
    private Map<String, Object> extraAttributes;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(Long code, String errorMessage) {
        this(errorMessage);
        this.code = code;
    }
}
