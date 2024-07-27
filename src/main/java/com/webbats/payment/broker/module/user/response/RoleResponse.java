package com.webbats.payment.broker.module.user.response;

import java.time.LocalDateTime;

public record RoleResponse(String roleId, String roleName, String roleDesc,
                           LocalDateTime createdAt, String createdBy, LocalDateTime lastModifiedAt,
                           String lastModifiedBy) {
}
