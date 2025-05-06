package com.shongon.mini_bank.controller.user;

import com.shongon.mini_bank.dto.request.role.AssignNRemoveRoleRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ApiResponse<String> lockUser(@PathVariable Long userId) {
        userService.lockUser(userId);
        return ApiResponse.<String>builder()
                .code(200)
                .result("User has been locked.")
                .build();
    }

    @PutMapping("/unlock/{userId}")
    public ApiResponse<String> unlockUser(@PathVariable Long userId) {
        userService.unlockUser(userId);
        return ApiResponse.<String>builder()
                .code(200)
                .result("User has been locked.")
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
    public ApiResponse<String> assignRole (@PathVariable Long userId, @RequestBody AssignNRemoveRoleRequest request) {
        userProfileService.assignRoleToUser(userId, request);
        return ApiResponse.<String>builder()
                .code(200)
                .result("Assign new role success.")
                .build();
    }

    // Remove a role from the user
    @PutMapping("/remove-role/{userId}")
    public ApiResponse<String> removeRole (@PathVariable Long userId, @RequestBody AssignNRemoveRoleRequest request) {
        userProfileService.removeRoleFromUser(userId, request);
        return ApiResponse.<String>builder()
                .code(200)
                .result("Remove role from user success.")
                .build();
    }

    @GetMapping("/logs")
    public ApiResponse<Page<AuditLogResponse>> getAllLogs(
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return ApiResponse.<Page<AuditLogResponse>>builder()
                .message("Audit logs retrieved successfully.")
                .result(auditLogService.getAllLogsPaginated(pageable))
                .build();
    }

    @GetMapping("/user-logs/{userId}")
    public ApiResponse<Page<AuditLogResponse>> getUserLogs(
            @PathVariable Long userId,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return ApiResponse.<Page<AuditLogResponse>>builder()
                .message("User audit logs retrieved successfully.")
                .result(auditLogService.getUserLogsPaginated(userId, pageable))
                .build();
    }

    @GetMapping("/entity-logs/{entityName}")
    public ApiResponse<Page<AuditLogResponse>> getEntityLogs(
            @PathVariable String entityName,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable)
    {
        return ApiResponse.<Page<AuditLogResponse>>builder()
                .message("Entity audit logs retrieved successfully.")
                .result(auditLogService.getEntityLogsPaginated(entityName, pageable))
                .build();
    }
}
