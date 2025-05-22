package com.shongon.audit_log.controller.user;

import com.shongon.audit_log.dto.request.user.RegisterRequest;
import com.shongon.audit_log.dto.request.user.UpdateUserInfoRequest;
import com.shongon.audit_log.dto.response.user.RegisterResponse;
import com.shongon.audit_log.dto.response.user.UpdateUserInfoResponse;
import com.shongon.audit_log.service.user.UserService;
import com.shongon.audit_log.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ApiResponse.<RegisterResponse>builder()
                .code(200)
                .result(userService.register(registerRequest))
                .build();
    }

    @PutMapping("/update/{userId}")
    public ApiResponse<UpdateUserInfoResponse> update(@PathVariable Long userId, @Valid @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        return ApiResponse.<UpdateUserInfoResponse>builder()
                .code(200)
                .result(userService.updateUserInfo(userId, updateUserInfoRequest))
                .build();
    }

}
