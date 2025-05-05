package com.shongon.mini_bank.controller.user;

import com.shongon.mini_bank.constant.status.UserStatus;
import com.shongon.mini_bank.dto.response.audit_log.AuditLogResponse;
import com.shongon.mini_bank.dto.response.user.ViewAllUsersResponse;
import com.shongon.mini_bank.dto.response.user.ViewUserProfileResponse;
import com.shongon.mini_bank.service.security.AuditLogService;
import com.shongon.mini_bank.service.user.UserProfileService;
import com.shongon.mini_bank.service.user.UserService;
import com.shongon.mini_bank.utils.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    UserProfileService userProfileService;
    UserService userService;
    AuditLogService auditLogService;

    // View all users
    @GetMapping("/view-all")
    public ApiResponse<List<ViewAllUsersResponse>> viewAllUsers() {
        return ApiResponse.<List<ViewAllUsersResponse>>builder()
                .code(200)
                .result(userService.getAllUsers())
                .build();
    }

    // View specific user by username
    @GetMapping("/view/{userId}")
    public ApiResponse<ViewUserProfileResponse> viewUserProfile(@PathVariable Long userId) {
        return ApiResponse.<ViewUserProfileResponse>builder()
                .code(200)
                .result(userService.getUserProfile(userId))
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
    @DeleteMapping("delete/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .code(200)
                .result("User has been deleted.")
                .build();
    }
    // Assign a role to the user
    @PutMapping("/assign-role/{userId}")
    public ApiResponse<String> assignRole (@PathVariable Long userId, @RequestBody String roleName) {
        userProfileService.assignRoleToUser(userId, roleName);
        return ApiResponse.<String>builder()
                .code(200)
                .result("Assign new role success.")
                .build();
    }

    // Remove a role from the user
    @PutMapping("/remove-role/{userId}")
    public ApiResponse<String> removeRole (@PathVariable Long userId, @RequestBody String roleName) {
        userProfileService.removeRoleFromUser(userId, roleName);
        return ApiResponse.<String>builder()
                .code(200)
                .result("Remove role from user success.")
                .build();
    }

    // Get all logs of the user
    @GetMapping("/user-logs/{userId}")
    public ApiResponse<List<AuditLogResponse>> getUserLogs(@PathVariable Long userId) {
        return ApiResponse.<List<AuditLogResponse>>builder()
                .code(200)
                .result(auditLogService.getUserLogs(userId))
                .build();
    }

    // Get all entity logs
    @GetMapping("/entity-logs/{entityName}")
    public ApiResponse<List<AuditLogResponse>> getEntityLogs(@PathVariable String entityName) {
        return ApiResponse.<List<AuditLogResponse>>builder()
                .code(200)
                .result(auditLogService.getEntityLogs(entityName))
                .build();
    }
}
