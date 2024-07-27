package com.webbats.payment.broker.common.service;

import com.webbats.payment.broker.common.entity.Language;
import com.webbats.payment.broker.common.entity.User;
import com.webbats.payment.broker.common.enums.BrokerEmailTemplateType;
import com.webbats.payment.broker.common.exception.Errors;
import com.webbats.payment.broker.common.exception.NotFoundException;
import com.webbats.payment.broker.common.repo.UserRepository;
import com.webbats.payment.common.module.email.request.EmailSendRequest;
import com.webbats.payment.common.module.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrokerEmailNotificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    private static String getUserLangId(User user) {
        return user.getLanguages().stream()
                .findFirst()
                .map(Language::getLangCode)
                .orElse("en");
    }

    public void sendUserPasswordChangeEmail(long userId, String password) {

        try {
            User user = getUser(userId);
            Map<String, Object> emailContext = new HashMap<>();
            emailContext.put("fullName", user.getFullName());
            emailContext.put("password", password);

            String userLangId = getUserLangId(user);

            emailService.sendEmail(EmailSendRequest.builder()
                    .to(user.getEmail())
                    .emailTemplateType(BrokerEmailTemplateType.BROKER_USER_PASSWORD_CHANGED.name())
                    .userLangId(userLangId)
                    .emailContextMap(emailContext)
                    .build()
            );


        } catch (Exception e) {
            log.error("Exception occurred while sending email", e);
        }
    }

    public void sendNewUserEmail(long userId, String password) {

        try {
            User user = getUser(userId);
            Map<String, Object> emailContext = new HashMap<>();
            emailContext.put("fullName", user.getFullName());
            emailContext.put("password", password);
            String userLangId = getUserLangId(user);

            emailService.sendEmail(EmailSendRequest.builder()
                    .to(user.getEmail())
                    .emailTemplateType(BrokerEmailTemplateType.BROKER_USER_ACCOUNT_CREATION.name())
                    .userLangId(userLangId)
                    .emailContextMap(emailContext)
                    .build()
            );

        } catch (Exception e) {
            log.error("Exception occurred while sending email", e);
        }

    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_NO_RECORD.message()));
    }

}
