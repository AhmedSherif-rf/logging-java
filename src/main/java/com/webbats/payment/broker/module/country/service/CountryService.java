package com.webbats.payment.broker.module.country.service;


import com.webbats.payment.broker.module.country.response.CountryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CountryService {

    Page<CountryDTO> getCurrencyList(String brokerId, Pageable pageable);

}