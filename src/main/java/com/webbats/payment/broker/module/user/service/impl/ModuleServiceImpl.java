package com.webbats.payment.broker.module.user.service.impl;


import com.webbats.payment.broker.common.entity.Authority;
import com.webbats.payment.broker.common.entity.Module;
import com.webbats.payment.broker.module.user.repo.ModuleRepository;
import com.webbats.payment.broker.module.user.response.AuthorityDTO;
import com.webbats.payment.broker.module.user.response.ModuleDTO;
import com.webbats.payment.broker.module.user.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;

    @Override
    public List<ModuleDTO> getAllModules() {
        return moduleRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::mapToModuleDTO)
                .collect(Collectors.toList());
    }

    private AuthorityDTO mapToAuthorityDTO(Authority authority) {
        return new AuthorityDTO(
                authority.getAuthorityCode(),
                authority.getAuthorityName(),
                authority.getAuthorityDesc()
        );
    }

    private ModuleDTO mapToModuleDTO(Module module) {
        Set<AuthorityDTO> authorityDTOs = module.getAuthorities()
                .stream()
                .map(this::mapToAuthorityDTO)
                .collect(Collectors.toSet());

        return ModuleDTO.builder()
                .moduleCode(module.getModuleCode())
                .moduleName(module.getModuleName())
                .moduleDesc(module.getModuleDesc())
                .authorities(authorityDTOs)
                .build();
    }
}
