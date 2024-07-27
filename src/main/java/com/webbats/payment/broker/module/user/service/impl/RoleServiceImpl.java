package com.webbats.payment.broker.module.user.service.impl;

import com.webbats.payment.broker.common.entity.Authority;
import com.webbats.payment.broker.common.entity.Module;
import com.webbats.payment.broker.common.entity.Role;
import com.webbats.payment.broker.common.entity.User;
import com.webbats.payment.broker.common.enums.UserType;
import com.webbats.payment.broker.common.exception.DuplicateRecordException;
import com.webbats.payment.broker.common.exception.NotFoundException;
import com.webbats.payment.broker.common.exception.RoleAssociatedException;
import com.webbats.payment.broker.common.repo.UserRepository;
import com.webbats.payment.broker.module.user.repo.AuthorityRepository;
import com.webbats.payment.broker.module.user.repo.ModuleRepository;
import com.webbats.payment.broker.module.user.repo.RoleRepository;
import com.webbats.payment.broker.module.user.request.CreateRoleRequest;
import com.webbats.payment.broker.module.user.request.UpdateRoleRequest;
import com.webbats.payment.broker.module.user.response.AuthorityDTO;
import com.webbats.payment.broker.module.user.response.ModuleDTO;
import com.webbats.payment.broker.module.user.response.RoleAuthorityResponse;
import com.webbats.payment.broker.module.user.response.RoleResponse;
import com.webbats.payment.broker.module.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public RoleAuthorityResponse createNewRole(CreateRoleRequest createRoleRequest) {
        validateCreateRoleRequest(createRoleRequest);
        Role role = new Role();
        role.setId(createRoleRequest.id());
        role.setName(createRoleRequest.name());
        role.setDescription(createRoleRequest.description());
        role.setUserTypeCode(UserType.BROKER);
        if (!Objects.isNull(createRoleRequest.authorities())) {
            Set<Authority> authorities = new HashSet<>();
            createRoleRequest.authorities().forEach(authorityCode -> {
                Authority authority = authorityRepository.findById(authorityCode)
                        .orElseThrow(() -> new NotFoundException("No authority found with authority code: " + authorityCode));
                authorities.add(authority);
            });
            role.setAuthorities(authorities);
        }
        Role savedRole = roleRepository.save(role);
        return mapToRoleAuthorityResponse(savedRole);
    }

    private void validateCreateRoleRequest(CreateRoleRequest createRoleRequest) {
        Optional<Role> roleById = roleRepository.findById(createRoleRequest.id());
        if (roleById.isPresent()) {
            log.info("role already exist with id: {}", createRoleRequest.id());
            throw new DuplicateRecordException("role already exist with id: " + createRoleRequest.id());
        }
        Optional<Role> roleByName = roleRepository.findByName(createRoleRequest.name());
        if (roleByName.isPresent()) {
            log.info("role already exist with name: {}", createRoleRequest.name());
            throw new DuplicateRecordException("role already exist with name: " + createRoleRequest.name());
        }
    }

    private RoleAuthorityResponse mapToRoleAuthorityResponse(Role role) {
        Set<String> authorityList = null;
        if (!Objects.isNull(role.getAuthorities())) {
            authorityList = role.getAuthorities().stream().map(Authority::getAuthorityCode).collect(Collectors.toSet());
        }
        return RoleAuthorityResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .authorities(authorityList)
                .createdAt(role.getCreatedAt())
                .createdBy(role.getCreatedBy())
                .lastModifiedAt(role.getUpdatedAt())
                .lastModifiedBy(role.getUpdatedBy())
                .build();
    }

    @Override
    public RoleResponse updateRole(String roleId, UpdateRoleRequest updateRoleRequest) {
        Role existingRole = validateUpdateRoleRequest(roleId, updateRoleRequest);
        existingRole.setName(updateRoleRequest.name());
        existingRole.setDescription(updateRoleRequest.description());
        Role updatedRole = roleRepository.save(existingRole);
        return mapToRoleResponse(updatedRole);
    }

    private Role validateUpdateRoleRequest(String roleId, UpdateRoleRequest updateRoleRequest) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("No role found with role id: " + roleId));
        Optional<Role> roleByName = roleRepository.findByName(updateRoleRequest.name());
        if (roleByName.isPresent() && !Objects.equals(roleByName.get().getId(), existingRole.getId())) {
            log.info("Role already exist with name: {}", updateRoleRequest.name());
            throw new DuplicateRecordException("role already exist with name: " + updateRoleRequest.name());
        }
        return existingRole;
    }

    @Override
    public List<String> updateRoleAuthorities(String roleId, List<String> authorityList) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("No role found with role id: " + roleId));
        Set<Authority> authorities = new HashSet<>();
        authorityList.forEach(authorityCode -> {
            Authority authority = authorityRepository.findById(authorityCode)
                    .orElseThrow(() -> new NotFoundException("No authority found with authority code: " + authorityCode));
            authorities.add(authority);
        });
        existingRole.setAuthorities(authorities);
        Role updatedRole = roleRepository.save(existingRole);
        return updatedRole.getAuthorities().stream().map(Authority::getAuthorityCode).toList();
    }

    @Override
    public List<RoleResponse> getRoles() {
        Set<Role> roles = roleRepository.findByUserTypeCode(UserType.BROKER);
        return roles.stream().map(this::mapToRoleResponse).toList();
    }

    @Override
    public List<String> getAuthoritiesForRole(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("No role found with role id: " + roleId));
        return role.getAuthorities().stream().map(Authority::getAuthorityCode).toList();
    }

    private RoleResponse mapToRoleResponse(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(), role.getCreatedAt(), role.getCreatedBy(),
                role.getCreatedAt(), role.getUpdatedBy());
    }

    @Override
    public void deleteRole(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("No role found with role id: " + roleId));
        List<User> userList = userRepository.findByRoles(role);
        if (!userList.isEmpty()) {
            throw new RoleAssociatedException("Can not delete role because It's associated with user.");
        }
        roleRepository.delete(role);
    }

    public List<ModuleDTO> getAllModules() {
        return moduleRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::mapToModuleDTO)
                .collect(Collectors.toList());
    }

    public ModuleDTO getAuthoritiesByModuleCode(String moduleCode) {
        Module module = moduleRepository.findById(moduleCode)
                .orElseThrow(() -> new NotFoundException("Module not found with code: " + moduleCode));
        return mapToModuleDTO(module);
    }

    public AuthorityDTO mapToAuthorityDTO(Authority authority) {
        return new AuthorityDTO(
                authority.getAuthorityCode(),
                authority.getAuthorityName(),
                authority.getAuthorityDesc()
        );
    }

    public ModuleDTO mapToModuleDTO(Module module) {
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
