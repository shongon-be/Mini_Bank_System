package com.shongon.mini_bank.dto.request.permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePermissionRequest {
    String permissionName;
    String permissionDescription;
}
