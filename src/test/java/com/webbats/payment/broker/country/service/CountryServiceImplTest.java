package com.webbats.payment.broker.country.service;

import com.webbats.payment.broker.common.response.PageResponse;
import com.webbats.payment.broker.integration.hub.response.CountryResponse;
import com.webbats.payment.broker.integration.hub.service.CountryIntegrationService;
import com.webbats.payment.broker.module.country.response.CountryDTO;
import com.webbats.payment.broker.module.country.service.CountryService;
import com.webbats.payment.broker.module.country.service.impl.CountryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CountryServiceImplTest {

    @Mock
    private CountryIntegrationService countryIntegrationService;

    private CountryService countryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        countryService = new CountryServiceImpl(countryIntegrationService);
    }

    @Test
    void getCurrencyList_ReturnsPageOfCountryDTO() {
        // Arrange
        String brokerId = "1";
        CountryResponse country = CountryResponse.builder()
                .countryCode("US")
                .countryName("United States")
                .currencyCode("USD")
                .isActive(true)
                .build();

        PageResponse<CountryResponse> countryPage = PageResponse.<CountryResponse>builder()
                .pageSize(10)
                .pageNumber(0)
                .totalElements(1)
                .content(Collections.singletonList(country))
                .build();

        Mockito.when(countryIntegrationService.getCurrencyList(any(String.class), any(Pageable.class))).thenReturn(countryPage);

        // Act
        Page<CountryDTO> result = countryService.getCurrencyList(brokerId, Mockito.mock(Pageable.class));

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("US", result.getContent().getFirst().getCountryCode());
    }
}
