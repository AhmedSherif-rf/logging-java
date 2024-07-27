package com.webbats.payment.broker.module.user.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean enabled;
    private List<String> languages;
    private boolean online;
    private boolean active;
    private boolean termsAccepted;
    private String country;
    private List<String> roles;
}
