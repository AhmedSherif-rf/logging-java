package com.webbats.payment.broker.common.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageDTO {
    private String code;
    private String name;
}
