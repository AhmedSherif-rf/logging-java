package com.webbats.payment.broker.integration.hub.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryResponse {
    private String countryCode;
    private String countryName;
    private String currencyCode;
    private Boolean isActive;
}