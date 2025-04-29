package com.shongon.mini_bank.mapper;

import com.shongon.mini_bank.dto.request.user.ChangePasswordRequest;
import com.shongon.mini_bank.dto.request.user.RegisterRequest;
import com.shongon.mini_bank.dto.request.user.UpdateUserInfoRequest;
import com.shongon.mini_bank.dto.response.user.*;
import com.shongon.mini_bank.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User registerUser (RegisterRequest registerRequest);

    void updateUser (@MappingTarget User user, UpdateUserInfoRequest updateUserInfoRequest);

    void changePassword (@MappingTarget User user, ChangePasswordRequest changePasswordRequest);

    RegisterResponse toRegisterResponse (User user);

    UpdateUserInfoResponse toUpdateUserInfoResponse (User user);

    ChangePasswordResponse toChangePasswordResponse (User user);

    ViewAllUsersResponse toViewAllUsersResponse (User user);

    ViewUserProfileResponse toViewUserProfileResponse (User user);
}
