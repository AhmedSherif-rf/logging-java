package com.webbats.payment.broker.module.broker.controller;

import com.webbats.payment.broker.common.constants.AppConstants;
import com.webbats.payment.broker.module.broker.request.LoginRequest;
import com.webbats.payment.broker.module.broker.response.BrokerLoginResponse;
import com.webbats.payment.broker.module.broker.service.BrokerAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.API_BROKER)
@RequiredArgsConstructor
@Tag(name = "Broker Controller", description = "Endpoints for broker")
@Slf4j
public class BrokerAuthController {

    private final BrokerAuthService brokerService;

    @PostMapping("/login")
    @Operation(summary = "Login endpoint for broker")
    public ResponseEntity<BrokerLoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        return brokerService.login(loginRequest, httpServletRequest);
    }
}
