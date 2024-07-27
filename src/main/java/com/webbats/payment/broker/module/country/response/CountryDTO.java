package com.webbats.payment.broker.module.country.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryDTO {
    private String countryCode;
    private String countryName;
    private String currencyCode;
    private Boolean isActive;
}
