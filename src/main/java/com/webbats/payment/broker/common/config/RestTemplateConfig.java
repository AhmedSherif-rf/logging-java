package com.webbats.payment.broker.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateConfig {

    @Value("${integration.hub.base-url}")
    private String baseUrl;

    @Bean
    RestTemplate hubRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        DefaultUriBuilderFactory uriFactory = new DefaultUriBuilderFactory(baseUrl);
        restTemplate.setUriTemplateHandler(uriFactory);
        return restTemplate;
    }
}
