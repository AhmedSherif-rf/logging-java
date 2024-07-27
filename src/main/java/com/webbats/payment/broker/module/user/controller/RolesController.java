package com.webbats.payment.broker.module.user.controller;

import com.webbats.payment.broker.common.constants.AppConstants;
import com.webbats.payment.broker.module.user.request.CreateRoleRequest;
import com.webbats.payment.broker.module.user.request.UpdateRoleRequest;
import com.webbats.payment.broker.module.user.response.RoleAuthorityResponse;
import com.webbats.payment.broker.module.user.response.RoleResponse;
import com.webbats.payment.broker.module.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(AppConstants.API_ROLE)
@RequiredArgsConstructor
@Tag(name = "Roles Controller", description = "Endpoints for roles")
@Slf4j
public class RolesController {

    private final RoleService roleService;

    @PostMapping
    @Operation(summary = "Create new role for broker")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_ROLES_WRITE)")
    public ResponseEntity<RoleAuthorityResponse> createRole(@Valid @RequestBody CreateRoleRequest createRoleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createNewRole(createRoleRequest));
    }

    @PutMapping("/{roleId}")
    @Operation(summary = "Update role")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_ROLES_WRITE)")
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody UpdateRoleRequest updateRoleRequest, @PathVariable String roleId) {
        return ResponseEntity.ok(roleService.updateRole(roleId, updateRoleRequest));
    }

    @PutMapping("/{roleId}/authorities")
    @Operation(summary = "Update role authorities")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_ROLES_WRITE)")
    public ResponseEntity<List<String>> updateRoleAuthority(@RequestBody List<String> authorityList, @PathVariable String roleId) {
        return ResponseEntity.ok(roleService.updateRoleAuthorities(roleId, authorityList));
    }

    @GetMapping
    @Operation(summary = "fetch all roles for broker")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_ROLES_READ)")
    public ResponseEntity<List<RoleResponse>> fetchRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @GetMapping("/{roleId}/authorities")
    @Operation(summary = "fetch all authorities of role")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_ROLES_READ)")
    public ResponseEntity<List<String>> fetchAuthoritiesForRole(@PathVariable String roleId) {
        return ResponseEntity.ok(roleService.getAuthoritiesForRole(roleId));
    }

    @DeleteMapping("/{roleId}")
    @Operation(summary = "Delete a role")
    @PreAuthorize("hasAuthority(@authorityConstants.BROKER_ROLES_DELETE)")
    public ResponseEntity<Void> deleteRole(@PathVariable String roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

}
