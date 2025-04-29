package com.shongon.mini_bank.controller.security;

import com.shongon.mini_bank.utils.ApiResponse;
import com.shongon.mini_bank.dto.request.permission.CreatePermissionRequest;
import com.shongon.mini_bank.dto.response.permission.CreatePermissionResponse;
import com.shongon.mini_bank.dto.response.permission.ViewAllPermissionsResponse;
import com.shongon.mini_bank.service.security.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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
