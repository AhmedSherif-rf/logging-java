package com.webbats.payment.broker.module.user.controller;

import com.webbats.payment.broker.common.constants.AppConstants;
import com.webbats.payment.broker.module.user.response.ModuleDTO;
import com.webbats.payment.broker.module.user.service.ModuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppConstants.API_BROKER + "/modules")
@RequiredArgsConstructor
@Tag(name = "Module Controller", description = "Endpoints for modules and authorities")
public class ModulessController {

    private final ModuleService moduleService;

    @GetMapping("/authorities")
    @Operation(summary = "Get all modules with authorities")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_ROLES_READ)")
    public ResponseEntity<List<ModuleDTO>> getAllModules() {
        List<ModuleDTO> modules = moduleService.getAllModules();
        return ResponseEntity.ok(modules);
    }

}
