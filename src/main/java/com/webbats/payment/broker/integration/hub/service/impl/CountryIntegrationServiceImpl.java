package com.webbats.payment.broker.integration.hub.service.impl;


import com.webbats.payment.broker.common.response.PageResponse;
import com.webbats.payment.broker.integration.hub.response.CountryResponse;
import com.webbats.payment.broker.integration.hub.service.CountryIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountryIntegrationServiceImpl implements CountryIntegrationService {


    private final RestTemplate hubRestTemplate;

    public PageResponse<CountryResponse> getCurrencyList(String brokerId, Pageable pageable) {
        ResponseEntity<PageResponse<CountryResponse>> response;
        try {
            String url = String.format("/api/client/brokers/%s/countries?page=%s&size=%s", brokerId, pageable.getPageNumber(), pageable.getPageSize());
            response = hubRestTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
            });
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching countries from hub", e);
        }
        throw new RuntimeException("Error occurred while processing request");
    }
}
