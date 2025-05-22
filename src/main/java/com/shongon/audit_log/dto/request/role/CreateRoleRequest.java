package com.shongon.audit_log.dto.request.role;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateRoleRequest {
    String roleName;
    String roleDescription;
    Set<String> permissions;
}
