package com.webbats.payment.broker.module.country.service.impl;

import com.webbats.payment.broker.common.response.PageResponse;
import com.webbats.payment.broker.integration.hub.response.CountryResponse;
import com.webbats.payment.broker.integration.hub.service.CountryIntegrationService;
import com.webbats.payment.broker.module.country.response.CountryDTO;
import com.webbats.payment.broker.module.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryIntegrationService countryIntegrationService;

    public Page<CountryDTO> getCurrencyList(String brokerId, Pageable pageable) {
        PageResponse<CountryResponse> countryList = countryIntegrationService.getCurrencyList(brokerId, pageable);
        return new PageImpl<>(
                countryList.getContent().stream()
                        .map(this::mapToCountryDTO)
                        .collect(Collectors.toList()),
                pageable,
                countryList.getTotalElements());
    }

    private CountryDTO mapToCountryDTO(CountryResponse country) {
        return CountryDTO.builder()
                .countryCode(country.getCountryCode())
                .countryName(country.getCountryName())
                .currencyCode(country.getCurrencyCode())
                .isActive(country.getIsActive())
                .build();
    }
}
