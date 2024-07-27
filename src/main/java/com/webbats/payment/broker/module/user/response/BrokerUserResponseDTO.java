package com.webbats.payment.broker.module.user.response;

import com.webbats.payment.broker.common.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BrokerUserResponseDTO {
    private long brokerId;
    private UserResponseDTO user;
    private UserStatus status;
    private boolean canHandleAppeals;
}
