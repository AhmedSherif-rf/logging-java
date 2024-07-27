package com.webbats.payment.broker.module.user.request;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CreateRoleRequest(
        @NotBlank(message = "Role id can't be empty") String id,
        @NotBlank(message = "Role name can't be empty") String name,
        String description,
        Set<String> authorities) {
}
