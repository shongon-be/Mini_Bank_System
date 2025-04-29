package com.shongon.mini_bank.dto.response.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordResponse {
    String message = "Change password success!";
}
