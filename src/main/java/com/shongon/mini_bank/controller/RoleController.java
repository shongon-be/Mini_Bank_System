package com.shongon.mini_bank.controller;

import com.shongon.mini_bank.dto.ApiResponse;
import com.shongon.mini_bank.dto.request.role.CreateRoleRequest;
import com.shongon.mini_bank.dto.request.role.UpdateRoleRequest;
import com.shongon.mini_bank.dto.response.role.CreateRoleResponse;
import com.shongon.mini_bank.dto.response.role.UpdateRoleResponse;
import com.shongon.mini_bank.dto.response.role.ViewAllRolesResponse;
import com.shongon.mini_bank.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;

    @PostMapping("/create")
    public ApiResponse<CreateRoleResponse> createRole(@RequestBody CreateRoleRequest request) {
        return ApiResponse.<CreateRoleResponse>builder()
                .code(200)
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping("view-all")
    public ApiResponse<List<ViewAllRolesResponse>> getAllRoles() {
        return ApiResponse.<List<ViewAllRolesResponse>>builder()
                .code(200)
                .result(roleService.getAllRoles())
                .build();
    }

    @PutMapping("/update/{roleName}")
    public ApiResponse<UpdateRoleResponse> updateRole(
            @PathVariable String roleName, @RequestBody UpdateRoleRequest request) {

        return ApiResponse.<UpdateRoleResponse>builder()
                .code(200)
                .result(roleService.updateRolePermissions(roleName, request))
                .build();
    }

    @DeleteMapping("/delete/{roleName}")
    public ApiResponse<String> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);

        return ApiResponse.<String>builder()
                .code(200)
                .result("Role has been deleted!")
                .build();
    }
}
