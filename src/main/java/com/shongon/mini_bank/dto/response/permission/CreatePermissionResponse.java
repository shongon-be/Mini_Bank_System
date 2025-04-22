package com.shongon.mini_bank.dto.response.permission;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePermissionResponse {
    String message = "Create permission success!";
}
