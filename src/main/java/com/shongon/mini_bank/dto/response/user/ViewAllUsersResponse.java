package com.shongon.mini_bank.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shongon.mini_bank.constant.status.UserStatus;
import com.shongon.mini_bank.dto.response.role.ViewAllRolesResponse;
import com.shongon.mini_bank.utils.CustomTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewAllUsersResponse {
    Long userId;

    String username;

    String password;

    String fullname;

    String email;

    String phoneNumber;

    LocalDate birthDate;

    UserStatus status;

    Set<ViewAllRolesResponse> roles;

    @JsonSerialize(using = CustomTime.class)
    LocalDateTime createdAt;

    @JsonSerialize(using = CustomTime.class)
    LocalDateTime updatedAt;

    @JsonSerialize(using = CustomTime.class)
    LocalDateTime lockedAt;

}
