package com.shongon.audit_log.mapper;

import com.shongon.audit_log.dto.request.role.CreateRoleRequest;
import com.shongon.audit_log.dto.request.role.UpdateRoleRequest;
import com.shongon.audit_log.dto.response.role.CreateRoleResponse;
import com.shongon.audit_log.dto.response.role.UpdateRoleResponse;
import com.shongon.audit_log.dto.response.role.ViewAllRolesResponse;
import com.shongon.audit_log.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toCreateRole(CreateRoleRequest request);

    @Mapping(target = "permissions", ignore = true)
    void toUpdateRole(@MappingTarget Role role, UpdateRoleRequest updateRequest);

    CreateRoleResponse toCreateRoleResponse(Role role);

    ViewAllRolesResponse toViewAllRolesResponse(Role role);

    UpdateRoleResponse toUpdateRoleResponse(Role role);
}
