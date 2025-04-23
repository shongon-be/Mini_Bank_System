package com.shongon.mini_bank.mapper;

import com.shongon.mini_bank.dto.request.permission.CreatePermissionRequest;
import com.shongon.mini_bank.dto.response.permission.CreatePermissionResponse;
import com.shongon.mini_bank.dto.response.permission.ViewAllPermissionsResponse;
import com.shongon.mini_bank.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper (componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {
    Permission createPermission(CreatePermissionRequest request);

    CreatePermissionResponse toCreatePermissionResponse(Permission permission);

    ViewAllPermissionsResponse toViewAllPermissionsResponse(Permission permission);
}
