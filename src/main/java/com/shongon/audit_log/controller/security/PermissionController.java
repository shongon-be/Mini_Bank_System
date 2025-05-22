package com.shongon.audit_log.controller.security;

import com.shongon.audit_log.utils.ApiResponse;
import com.shongon.audit_log.dto.request.permission.CreatePermissionRequest;
import com.shongon.audit_log.dto.response.permission.CreatePermissionResponse;
import com.shongon.audit_log.dto.response.permission.ViewAllPermissionsResponse;
import com.shongon.audit_log.service.security.PermissionService;
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
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping("/create")
    public ApiResponse<CreatePermissionResponse> createPermission(@RequestBody CreatePermissionRequest request) {
        return ApiResponse.<CreatePermissionResponse>builder()
                .code(200)
                .result(permissionService.createPermission(request))
                .build();
    }

    @GetMapping("/view-all")
    public ApiResponse<List<ViewAllPermissionsResponse>> viewAllPermissions() {
        return ApiResponse.<List<ViewAllPermissionsResponse>>builder()
                .code(200)
                .result(permissionService.getAllPermissions())
                .build();
    }

    @DeleteMapping("/delete/{permissionName}")
    public ApiResponse<String> deletePermission(@PathVariable String permissionName) {
        permissionService.deletePermission(permissionName);
        return ApiResponse.<String>builder()
                .code(200)
                .result("Permission has been deleted")
                .build();
    }
}
