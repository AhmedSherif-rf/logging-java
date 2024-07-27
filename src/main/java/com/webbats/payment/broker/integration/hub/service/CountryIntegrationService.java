package com.webbats.payment.broker.integration.hub.service;

import com.webbats.payment.broker.common.response.PageResponse;
import com.webbats.payment.broker.integration.hub.response.CountryResponse;
import org.springframework.data.domain.Pageable;


public interface CountryIntegrationService {

    PageResponse<CountryResponse> getCurrencyList(String brokerId, Pageable pageable);

}