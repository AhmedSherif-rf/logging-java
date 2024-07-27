package com.webbats.payment.broker.module.user.response;

import com.webbats.payment.broker.common.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BrokerUserPageResponseDto {
    private Long brokerId;
    private String fullName;
    private String emailAddress;
    private String countryCode;
    private List<String> roles;
    private UserStatus status;
    private List<String> languages;
}
