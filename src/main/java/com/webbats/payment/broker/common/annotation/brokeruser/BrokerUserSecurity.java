package com.webbats.payment.broker.common.annotation.brokeruser;

import com.webbats.payment.broker.common.config.JwtService;
import com.webbats.payment.broker.common.enums.UserType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class BrokerUserSecurity {

    private final JwtService jwtService;

    public boolean isBrokerUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = jwtService.extractTokenFromRequest(request);
        if (token == null) {
            return false;
        }
        Claims claims = jwtService.extractAllClaims(token);
        String userType = claims.get("type", String.class);
        return UserType.BROKER.toString().equals(userType);
    }
}