package com.webbats.payment.broker.common.config;

import com.webbats.payment.broker.common.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    private static boolean isPrincipalAuthenticated(Authentication principal) {
        return principal != null &&
                !(principal instanceof AnonymousAuthenticationToken) &&
                principal.isAuthenticated();
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            Authentication principal = SecurityContextHolder.getContext().getAuthentication();
            if (isPrincipalAuthenticated(principal)) {
                if (principal.getPrincipal() instanceof User userObj) {
                    String userId = userObj.getId().toString();
                    return Optional.ofNullable(userId);
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while retrieving the current auditor: " + e.getMessage());
        }
        return Optional.empty();
    }
}
