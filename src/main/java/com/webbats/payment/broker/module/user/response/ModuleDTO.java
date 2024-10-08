package com.webbats.payment.broker.module.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleDTO {
    private String moduleCode;
    private String moduleName;
    private String moduleDesc;
    private Set<AuthorityDTO> authorities;
}
