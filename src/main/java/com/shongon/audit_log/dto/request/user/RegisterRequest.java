package com.shongon.audit_log.dto.request.user;

import com.shongon.audit_log.utils.validation.DobConstraint;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 3, max = 50, message = "Username must be between 3-50 characters.")
    String username;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character.")
    String password;

    @NotBlank(message = "You must enter your full name.")
    String fullname;

    @NotBlank(message = "Email cannot be empty.")
    @Email
    String email;

    @NotBlank(message = "Phone number cannot be empty.")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number must be at least 10 digits.")
    String phoneNumber;

    @DobConstraint(min = 18, message = "Your age must be at least {min} years old")
    @NotNull(message = "You must enter your date of birth.")
    LocalDate birthDate;
}
