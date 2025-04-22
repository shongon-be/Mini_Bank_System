package com.shongon.mini_bank.dto.response.role;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRoleResponse {
    String message = "Update role success!";
}
