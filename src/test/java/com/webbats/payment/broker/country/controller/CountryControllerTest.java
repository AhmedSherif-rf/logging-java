package com.webbats.payment.broker.country.controller;

import com.webbats.payment.broker.common.response.PageResponse;
import com.webbats.payment.broker.integration.hub.response.CountryResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = false)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate hubRestTemplate;


    @BeforeAll
    void setUp() {

    }

    @BeforeEach
    void setBeforeEach() {

    }

    @Test
    void getCountries_ReturnsCountryList() throws Exception {
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

        String url = String.format("/api/client/brokers/%s/countries?page=%s&size=%s", brokerId, countryPage.getPageNumber(), countryPage.getPageSize());
        doReturn(ResponseEntity.ok(countryPage)).when(hubRestTemplate).exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<PageResponse<CountryResponse>>() {
        });
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/country?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].countryCode").value("US"));
    }
}
