package com.shongon.audit_log.mapper;

import com.shongon.audit_log.dto.request.user.ChangePasswordRequest;
import com.shongon.audit_log.dto.request.user.RegisterRequest;
import com.shongon.audit_log.dto.request.user.UpdateUserInfoRequest;
import com.shongon.audit_log.dto.response.user.*;
import com.shongon.audit_log.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User registerUser (RegisterRequest registerRequest);

    void updateUser (@MappingTarget User user, UpdateUserInfoRequest updateUserInfoRequest);

    void changePassword (@MappingTarget User user, ChangePasswordRequest changePasswordRequest);

    @Mapping(target = "message", constant = "Registered successfully!")
    RegisterResponse toRegisterResponse (User user);

    UpdateUserInfoResponse toUpdateUserInfoResponse (User user);

    ChangePasswordResponse toChangePasswordResponse (User user);

    ViewAllUsersResponse toViewAllUsersResponse (User user);

    ViewUserProfileResponse toViewUserProfileResponse (User user);
}
