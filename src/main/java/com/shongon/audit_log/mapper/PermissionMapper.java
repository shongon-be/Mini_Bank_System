package com.shongon.audit_log.mapper;

import com.shongon.audit_log.dto.request.permission.CreatePermissionRequest;
import com.shongon.audit_log.dto.response.permission.CreatePermissionResponse;
import com.shongon.audit_log.dto.response.permission.ViewAllPermissionsResponse;
import com.shongon.audit_log.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper (componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {
    Permission createPermission(CreatePermissionRequest request);

    CreatePermissionResponse toCreatePermissionResponse(Permission permission);

    ViewAllPermissionsResponse toViewAllPermissionsResponse(Permission permission);
}
