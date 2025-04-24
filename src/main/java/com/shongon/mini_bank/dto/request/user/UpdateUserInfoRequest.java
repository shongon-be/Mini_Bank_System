package com.shongon.mini_bank.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserInfoRequest {
    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character.")
    String password;

    @NotBlank(message = "Email cannot be empty.")
    @Email
    String email;

    @NotBlank(message = "Phone number cannot be empty.")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number must be at least 10 digits.")
    String phoneNumber;
}
