package com.shongon.mini_bank.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shongon.mini_bank.constant.status.UserStatus;
import com.shongon.mini_bank.dto.response.role.ViewAllRolesResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewUserProfileResponse {
    Long userId;

    String username;

    String password;

    String fullname;

    String email;

    String phoneNumber;

    LocalDate birthDate;

    UserStatus status;

    Set<ViewAllRolesResponse> roles;

//    Map<String, Account> accountTable; -- View all account by fullname

}
