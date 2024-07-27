package com.webbats.payment.broker.module.broker.service;

import com.webbats.payment.broker.common.config.JwtService;
import com.webbats.payment.broker.common.entity.RefreshToken;
import com.webbats.payment.broker.common.entity.Role;
import com.webbats.payment.broker.common.entity.User;
import com.webbats.payment.broker.common.enums.UserType;
import com.webbats.payment.broker.common.exception.Errors;
import com.webbats.payment.broker.common.exception.ValidationFailureException;
import com.webbats.payment.broker.common.repo.UserRepository;
import com.webbats.payment.broker.module.broker.request.LoginRequest;
import com.webbats.payment.broker.module.broker.response.BrokerLoginResponse;
import com.webbats.payment.broker.utility.DeviceFingerprintGenerator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrokerAuthService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public ResponseEntity<BrokerLoginResponse> login(LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        log.info("Broker Login request");
        Optional<User> optionalUser = userRepository.findByEmailAndUserType(loginRequest.getEmail(), UserType.BROKER);
        //TODO:: TBD if its required or not
   /*     String decryptedPassword = EncryptorAesGcm.decrypt(loginRequest.getPassword(), loginRequest.getEmail());
        loginRequest.setPassword(decryptedPassword);
        if (!(decryptedPassword.matches(AppConstants.PASSWORD_REGEX))) {
            throw new CustomException(Errors.PASSWORD_VALIDATION);
        }*/
        if (optionalUser.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword())) {
            log.error("Invalid login credentials");
            throw new ValidationFailureException(Errors.ERROR_INVALID_CREDENTIALS);
        }

        User user = optionalUser.get();
        if (!user.isEnabled()) {
            log.error("InActive User");
            throw new ValidationFailureException(Errors.ERROR_INACTIVE_USER);
        }

        log.info("Generating access token for user");
        String jwtToken = jwtService.generateToken(user);
        jwtService.saveTokenToDB(user, jwtToken, DeviceFingerprintGenerator.generateDeviceFingerprintFromHttpRequest(httpServletRequest));
        Claims claims = jwtService.extractAllClaims(jwtToken);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        Long expirationTimestamp = claims.getExpiration().getTime();
        Long currentTime = System.currentTimeMillis();
        Long timeRemainingInSeconds = (expirationTimestamp - currentTime) / 1000;
        Long refreshTokenExpirationTimestamp = refreshToken.getExpiry().toEpochMilli();
        Long refreshTokenTimeRemainingInSeconds = (refreshTokenExpirationTimestamp - currentTime) / 1000;
        log.info("Time remaining for the generated token to expire: {} seconds", timeRemainingInSeconds);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        BrokerLoginResponse loginResponse = BrokerLoginResponse.builder()
//                .countryCode(user.getCountry().getCountryCode())
                .expirationTime(expirationTimestamp)
                .timeRemainingInSeconds(timeRemainingInSeconds)
                .token(jwtToken).refreshToken(refreshToken.getRefreshToken())
                .refreshTokenExpirationTime(refreshTokenExpirationTimestamp)
                .refreshTokenTimeRemainingInSeconds(refreshTokenTimeRemainingInSeconds)
                .userId(user.getId())
                .roles(user.getRoles().stream().map(Role::getId).collect(Collectors.toSet()))
                .status(user.getStatus())
                .userName(user.getFullName())
                .email(user.getEmail())
//                .brokerId(broker.getBrokerId())
//                .canHandleAppeal(admin.isCanHandleAppeals())
                .build();
        return ResponseEntity.ok(loginResponse);
    }
}
