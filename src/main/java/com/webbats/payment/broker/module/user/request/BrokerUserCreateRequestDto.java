package com.webbats.payment.broker.module.user.request;

import com.webbats.payment.broker.common.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BrokerUserCreateRequestDto {

    private UserRequestDTO user;

    private boolean canHandleAppeals = false;

    @NotNull
    private UserStatus status = UserStatus.ACTIVE;
}