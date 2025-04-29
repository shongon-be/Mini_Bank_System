package com.shongon.mini_bank.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotBlank(message = "Old password cannot be empty.")
    String oldPassword;

    @NotBlank(message = "New password cannot be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character.")
    String newPassword;
}
