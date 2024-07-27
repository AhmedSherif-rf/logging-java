package com.webbats.payment.broker.module.country.controller;


import com.webbats.payment.broker.common.constants.AppConstants;
import com.webbats.payment.broker.module.country.response.CountryDTO;
import com.webbats.payment.broker.module.country.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(AppConstants.API_VERSION_1 + "/country")
@RequiredArgsConstructor
@Tag(name = "Country Controller", description = "Endpoints providing apis fetch Country")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    @Operation(summary = "Endpoint for getting countries")
    public Page<CountryDTO> getCountries(Pageable pageable) {
        //TODO:: fetch broker id from configs
        String brokerId = "1";
        return countryService.getCurrencyList(brokerId, pageable);
    }

}