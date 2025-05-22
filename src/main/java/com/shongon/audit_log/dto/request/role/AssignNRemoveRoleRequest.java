package com.shongon.audit_log.dto.request.role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignNRemoveRoleRequest {
    String roleName;
}
