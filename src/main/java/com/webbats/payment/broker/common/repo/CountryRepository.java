package com.webbats.payment.broker.common.repo;

import com.webbats.payment.broker.common.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    boolean existsByCountryName(String countryName);

    @Query("select (count(c) > 0) from Country c where c.currency.currencyCode = ?1")
    boolean existsByCurrencyCode(String currencyCode);
}