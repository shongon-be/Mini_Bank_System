package com.shongon.audit_log.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserInfoRequest {
    @NotBlank(message = "Email cannot be empty.")
    @Email
    String email;

    @NotBlank(message = "Phone number cannot be empty.")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number must be at least 10 digits.")
    String phoneNumber;
}
