package com.webbats.payment.broker.module.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {
    private String authorityCode;
    private String authorityName;
    private String authorityDesc;
}
