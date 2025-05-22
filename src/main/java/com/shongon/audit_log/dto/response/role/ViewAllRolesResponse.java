package com.shongon.audit_log.dto.response.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shongon.audit_log.dto.response.permission.ViewAllPermissionsResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewAllRolesResponse {
    String roleName;

    String roleDescription;

    Set<ViewAllPermissionsResponse> permissions;
}
