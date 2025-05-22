package com.shongon.audit_log.controller.user;

import com.shongon.audit_log.dto.request.user.ChangePasswordRequest;
import com.shongon.audit_log.dto.response.user.ChangePasswordResponse;
import com.shongon.audit_log.dto.response.user.ViewUserProfileResponse;
import com.shongon.audit_log.service.user.UserProfileService;
import com.shongon.audit_log.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users/profiles")
public class UserProfileController {
    UserProfileService userProfileService;

    @GetMapping("/view")
    public ApiResponse<ViewUserProfileResponse> viewMyProfile() {
        return ApiResponse.<ViewUserProfileResponse>builder()
                .code(200)
                .result(userProfileService.getMyInfo())
                .build();
    }

    @PutMapping("/change-password")
    public ApiResponse<ChangePasswordResponse> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        return ApiResponse.<ChangePasswordResponse>builder()
                .code(200)
                .result(userProfileService.changePassword(changePasswordRequest))
                .build();
    }
}

