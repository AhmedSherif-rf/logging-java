package com.webbats.payment.broker.module.user.controller;

import com.webbats.payment.broker.common.constants.AppConstants;
import com.webbats.payment.broker.common.entity.User;
import com.webbats.payment.broker.common.response.PageResponse;
import com.webbats.payment.broker.module.user.request.BrokerUserCreateRequestDto;
import com.webbats.payment.broker.module.user.request.BrokerUserFilterCriteria;
import com.webbats.payment.broker.module.user.request.BrokerUserUpdateRequest;
import com.webbats.payment.broker.module.user.response.BrokerUserPageResponseDto;
import com.webbats.payment.broker.module.user.response.BrokerUserResponseDTO;
import com.webbats.payment.broker.module.user.service.BrokerUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.API_BROKER + "/users")
@RequiredArgsConstructor
@Tag(name = "Broker User Controller", description = "Endpoints for managing broker users")
@Slf4j
public class BrokerUsersController {

    private final BrokerUserService brokerUserService;


    @GetMapping
    @Operation(summary = "Get broker user list")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_USERS_READ)")
    public PageResponse<BrokerUserPageResponseDto> getAdminUsers(@ModelAttribute BrokerUserFilterCriteria brokerUserFilterCriteria) {
        return brokerUserService.getBrokerUsers(brokerUserFilterCriteria);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get broker user by userId")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_USERS_READ)")
    public BrokerUserResponseDTO getAdminById(@PathVariable Long userId) {
        return brokerUserService.getBrokerUserById(userId);
    }

    @PutMapping(path = "{userId}")
    @Operation(summary = "Update broker user")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_USERS_WRITE)")
    public ResponseEntity<BrokerUserResponseDTO> updateAdmin(@Valid @RequestBody BrokerUserUpdateRequest request,
                                                             @PathVariable Long userId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(brokerUserService.updateBrokerUser(request, userId, user));
    }

    @PostMapping
    @Operation(summary = "Create new broker user")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_USERS_WRITE)")
    public ResponseEntity<BrokerUserResponseDTO> createBrokerUser(@Valid @RequestBody BrokerUserCreateRequestDto userCreateRequestDto) {
        BrokerUserResponseDTO broker = brokerUserService.createBrokerUser(userCreateRequestDto);
        return new ResponseEntity<>(broker, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/reset-password")
    @Operation(summary = "Reset broker user password")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_USERS_WRITE)")
    public ResponseEntity<Void> changeBrokerUserPassword(@PathVariable Long userId, @AuthenticationPrincipal User user) {
        brokerUserService.changeBrokerUserPassword(userId, user);
        return ResponseEntity.noContent().build();
    }
}
