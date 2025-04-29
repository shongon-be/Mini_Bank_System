package com.shongon.mini_bank.controller.user;

import com.shongon.mini_bank.constant.status.UserStatus;
import com.shongon.mini_bank.dto.response.user.ViewAllUsersResponse;
import com.shongon.mini_bank.dto.response.user.ViewUserProfileResponse;
import com.shongon.mini_bank.service.user.UserProfileService;
import com.shongon.mini_bank.service.user.UserService;
import com.shongon.mini_bank.utils.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
public class AdminController {
    UserProfileService userProfileService;
    UserService userService;

    // View all users
    @GetMapping("/view-all")
    public ApiResponse<List<ViewAllUsersResponse>> viewAllUsers() {
        return ApiResponse.<List<ViewAllUsersResponse>>builder()
                .code(200)
                .result(userService.getAllUsers())
                .build();
    }

    // View specific user by username
    @GetMapping("/view/{username}")
    public ApiResponse<ViewUserProfileResponse> viewUserProfile(@PathVariable String username) {
        return ApiResponse.<ViewUserProfileResponse>builder()
                .code(200)
                .result(userService.getUserProfile(username))
                .build();
    }

    // Lock user
    @PutMapping("/lock/{userId}")
    public ApiResponse<String> lockUser(@PathVariable Long userId, @RequestBody UserStatus userStatus) {
        userService.lockUser(userId, userStatus);
        return ApiResponse.<String>builder()
                .code(200)
                .result("User has been locked/unlocked.")
                .build();
    }

    // Delete user
//    @DeleteMapping("delete/userId")
    public ApiResponse<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .code(200)
                .result("User has been deleted.")
                .build();
    }
    // Assign role to user
    @PutMapping("/assign-role/{userId}")
    public ApiResponse<String> assignRole (@PathVariable Long userId, @RequestBody String roleName) {
        userProfileService.assignRoleToUser(userId, roleName);
        return ApiResponse.<String>builder()
                .code(200)
                .result("Assign new role success.")
                .build();
    }

    // Remove role from user
    @PutMapping("/remove-role/{userId}")
    public ApiResponse<String> removeRole (@PathVariable Long userId, @RequestBody String roleName) {
        userProfileService.removeRoleFromUser(userId, roleName);
        return ApiResponse.<String>builder()
                .code(200)
                .result("Remove role from user success.")
                .build();
    }
}
