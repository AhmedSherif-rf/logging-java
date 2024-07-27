package com.webbats.payment.broker.module.broker.response;

import com.webbats.payment.broker.common.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerLoginResponse {

    private String countryCode;
    private String token;
    private Long expirationTime;
    private Long timeRemainingInSeconds;
    private String refreshToken;
    private Long refreshTokenExpirationTime;
    private Long refreshTokenTimeRemainingInSeconds;
    private Long userId;
    private Set<String> roles;
    private String userName;
    private String email;
    private Long brokerId;
    private UserStatus status;
    private Boolean canHandleAppeal;
}
