package com.shongon.audit_log.service.security;

import com.shongon.audit_log.dto.request.permission.CreatePermissionRequest;
import com.shongon.audit_log.dto.response.permission.CreatePermissionResponse;
import com.shongon.audit_log.dto.response.permission.ViewAllPermissionsResponse;
import com.shongon.audit_log.exception.AppException;
import com.shongon.audit_log.exception.ErrorCode;
import com.shongon.audit_log.mapper.PermissionMapper;
import com.shongon.audit_log.model.Permission;
import com.shongon.audit_log.model.Role;
import com.shongon.audit_log.repository.PermissionRepository;
import com.shongon.audit_log.repository.RoleRepository;
import com.shongon.audit_log.utils.audit.Auditable;
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
    @Auditable(action = "CREATE_PERMISSION", entityType = "PERMISSION")
    public CreatePermissionResponse createPermission(CreatePermissionRequest request) {
        if (permissionRepository.existsByPermissionName((request.getPermissionName())))
            throw new AppException(ErrorCode.PERMISSION_EXISTED);

        Permission permission = permissionMapper.createPermission(request);

        return permissionMapper.toCreatePermissionResponse(permissionRepository.save(permission));
    }

    // Get a list of permissions
    @Transactional
    @Auditable(action = "VIEW_ALL_PERMISSIONS", entityType = "PERMISSION")
    public List<ViewAllPermissionsResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toViewAllPermissionsResponse)
                .toList();
    }

    @Transactional
    @Auditable(action = "DELETE_PERMISSION", entityType = "PERMISSION")
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
