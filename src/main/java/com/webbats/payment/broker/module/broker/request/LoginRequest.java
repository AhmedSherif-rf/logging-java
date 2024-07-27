package com.webbats.payment.broker.module.broker.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(example = "{\n" +
                "  \"email\": \"broker@webbats.com\",\n" +
                "  \"password\": \"Temp/123\",\n" +
                "  \"termsAccepted\": true\n" +
                "}")
public class LoginRequest {
    @NotBlank(message = "3000")
    @Email(message = "3001")
    private String email;
    @NotBlank(message = "3000")
    private String password;
    private boolean termsAccepted;
}