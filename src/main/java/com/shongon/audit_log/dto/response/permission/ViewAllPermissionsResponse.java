package com.shongon.audit_log.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewAllPermissionsResponse {
    String permissionName;
    String permissionDescription;
}
