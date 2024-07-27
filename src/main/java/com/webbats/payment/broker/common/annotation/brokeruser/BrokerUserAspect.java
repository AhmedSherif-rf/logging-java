package com.webbats.payment.broker.common.annotation.brokeruser;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class BrokerUserAspect {

    private final BrokerUserSecurity brokerUserSecurity;

    @Before("@annotation(com.webbats.payment.broker.common.annotation.brokeruser.BrokerUser)")
    public void checkAdminUser() {
        if (!brokerUserSecurity.isBrokerUser()) {
            throw new AuthorizationServiceException("Access denied");
        }
    }
}