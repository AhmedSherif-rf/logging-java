package com.webbats.payment.broker.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webbats.payment.broker.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationEntryPoint {

    Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        LOGGER.error("Authentication exception ", authException);
        ErrorResponse errorResponse = new ErrorResponse(Long.valueOf(HttpServletResponse.SC_UNAUTHORIZED), "Unable to authenticate the user.");
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
