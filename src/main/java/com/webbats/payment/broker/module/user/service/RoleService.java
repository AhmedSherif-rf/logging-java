package com.webbats.payment.broker.module.user.service;


import com.webbats.payment.broker.module.user.request.CreateRoleRequest;
import com.webbats.payment.broker.module.user.request.UpdateRoleRequest;
import com.webbats.payment.broker.module.user.response.ModuleDTO;
import com.webbats.payment.broker.module.user.response.RoleAuthorityResponse;
import com.webbats.payment.broker.module.user.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleAuthorityResponse createNewRole(CreateRoleRequest createRoleRequest);

    RoleResponse updateRole(String roleId, UpdateRoleRequest updateRoleRequest);

    List<String> updateRoleAuthorities(String roleId, List<String> authorityList);

    List<RoleResponse> getRoles();

    List<String> getAuthoritiesForRole(String roleId);

    void deleteRole(String roleId);

    List<ModuleDTO> getAllModules();

    ModuleDTO getAuthoritiesByModuleCode(String moduleCode);
}
