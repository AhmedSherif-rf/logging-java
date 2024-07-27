package com.webbats.payment.broker.module.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRequestDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String phoneNumber;
    private boolean enabled;
    @Size(min = 1)
    private List<String> languages;
    private boolean active;
    private boolean termsAccepted;
    @NotBlank
    private String country;
    @NotEmpty
    private List<String> roles;
}
