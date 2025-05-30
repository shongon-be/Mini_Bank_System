package com.shongon.mini_bank.service.security;

import com.shongon.mini_bank.dto.request.role.CreateRoleRequest;
import com.shongon.mini_bank.dto.request.role.UpdateRoleRequest;
import com.shongon.mini_bank.dto.response.role.CreateRoleResponse;
import com.shongon.mini_bank.dto.response.role.UpdateRoleResponse;
import com.shongon.mini_bank.dto.response.role.ViewAllRolesResponse;
import com.shongon.mini_bank.exception.AppException;
import com.shongon.mini_bank.exception.ErrorCode;
import com.shongon.mini_bank.mapper.RoleMapper;
import com.shongon.mini_bank.model.Permission;
import com.shongon.mini_bank.model.Role;
import com.shongon.mini_bank.repository.PermissionRepository;
import com.shongon.mini_bank.repository.RoleRepository;

import com.shongon.mini_bank.utils.audit.Auditable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    // Create new role
    @Transactional
    @Auditable(action = "CREATE_ROLE", entityType = "ROLE")
    public CreateRoleResponse createRole(CreateRoleRequest request) {
        if (roleRepository.existsByRoleName(request.getRoleName()))
            throw new AppException(ErrorCode.ROLE_EXISTED);

        Role role = roleMapper.toCreateRole(request);

        role.setPermissions(new HashSet<>());

        if (request.getPermissions() != null) {
            for (String permName : request.getPermissions()) {
                Permission permission = permissionRepository.findByPermissionName(permName)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
                role.getPermissions().add(permission);
            }
        }

        return roleMapper.toCreateRoleResponse(roleRepository.save(role));
    }


    // View all roles
    @Auditable(action = "VIEW_ALL_ROLES", entityType = "ROLE")
    @Transactional (readOnly = true)
    public List<ViewAllRolesResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toViewAllRolesResponse)
                .toList();
    }


    // Update permissions of role
    @Transactional
    @Auditable(action = "UPDATE_ROLE_PERMISSIONS", entityType = "ROLE")
    public UpdateRoleResponse updateRolePermissions(String roleName, UpdateRoleRequest request) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        roleMapper.toUpdateRole(role, request);

        role.getPermissions().clear();

        if (request.getPermissions() != null) {
            for (String permName : request.getPermissions()) {
                Permission permission = permissionRepository.findByPermissionName(permName)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
                role.getPermissions().add(permission);
            }
        }

        return roleMapper.toUpdateRoleResponse(roleRepository.save(role));
    }

    @Transactional
    @Auditable(action = "DELETE_ROLE", entityType = "ROLE")
    public void deleteRole(String roleName) {
        if (!roleRepository.existsByRoleName(roleName)) throw new AppException(ErrorCode.ROLE_NOT_FOUND);

        roleRepository.deleteByRoleName(roleName);
    }
}
