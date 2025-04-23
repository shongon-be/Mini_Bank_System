package com.shongon.mini_bank.service;

import com.shongon.mini_bank.dto.request.permission.CreatePermissionRequest;
import com.shongon.mini_bank.dto.response.permission.CreatePermissionResponse;
import com.shongon.mini_bank.dto.response.permission.ViewAllPermissionsResponse;
import com.shongon.mini_bank.exception.AppException;
import com.shongon.mini_bank.exception.ErrorCode;
import com.shongon.mini_bank.mapper.PermissionMapper;
import com.shongon.mini_bank.model.Permission;
import com.shongon.mini_bank.model.Role;
import com.shongon.mini_bank.repository.PermissionRepository;
import com.shongon.mini_bank.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    RoleRepository roleRepository;

    // Create permission
    @Transactional
    public CreatePermissionResponse createPermission(CreatePermissionRequest request) {
        if (permissionRepository.existsByPermissionName((request.getPermissionName())))
            throw new AppException(ErrorCode.PERMISSION_EXISTED);

        Permission permission = permissionMapper.createPermission(request);

        return permissionMapper.toCreatePermissionResponse(permissionRepository.save(permission));
    }

    // Get list of permissions
    @Transactional
    public List<ViewAllPermissionsResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toViewAllPermissionsResponse)
                .toList();
    }

    @Transactional
    public void deletePermission(String permissionName) {
        Permission permission = permissionRepository.findByPermissionName(permissionName)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        // Xóa quan hệ với các roles trước
        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            role.getPermissions().removeIf(p -> p.getPermissionName().equals(permissionName));
            roleRepository.save(role);
        }

        // Sau đó xóa permission
        permissionRepository.delete(permission);
    }
}
