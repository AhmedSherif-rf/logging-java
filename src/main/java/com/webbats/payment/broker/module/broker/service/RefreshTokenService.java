package com.webbats.payment.broker.module.broker.service;

import com.webbats.payment.broker.common.entity.RefreshToken;
import com.webbats.payment.broker.common.entity.User;
import com.webbats.payment.broker.common.exception.Errors;
import com.webbats.payment.broker.common.exception.ValidationFailureException;
import com.webbats.payment.broker.common.repo.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


/**
 * This service class is responsible for managing refresh tokens, including creation and verification.
 * Refresh tokens are used to obtain new access tokens after the expiration of the original access token.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${refresh.token.validity}")
    private long refreshTokenValidity;// Refresh token validity period in milliseconds

    /**
     * Create or update a refresh token for a given username.
     *
     * @param user The username for which the refresh token is created or updated.
     * @return The created or updated RefreshToken.
     */
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString())
                .expiry(Instant.now().plusMillis(refreshTokenValidity))
                .user(user)
                .build();
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    /**
     * Verify the validity of a refresh token.
     *
     * @param refreshToken The refresh token to verify.
     * @return The verified RefreshToken.
     * @throws RuntimeException if the refresh token is invalid or expired.
     */
    public RefreshToken verifyRefreshToken(String refreshToken) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (optionalRefreshToken.isEmpty()) {
            log.warn("Session Expired");
            throw new ValidationFailureException(Errors.ERROR_SESSION_EXPIRED);
        }

        RefreshToken tempRefreshToken = optionalRefreshToken.get();

        if (tempRefreshToken.getExpiry().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(tempRefreshToken);
            log.warn("Session Expired");
            throw new ValidationFailureException(Errors.ERROR_SESSION_EXPIRED);
        } else {
            tempRefreshToken.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
            tempRefreshToken.setRefreshToken(UUID.randomUUID().toString());
            refreshTokenRepository.save(tempRefreshToken);

            return tempRefreshToken;
        }
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}