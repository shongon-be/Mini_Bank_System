package com.shongon.audit_log.controller.auth;



import java.text.ParseException;

import com.shongon.audit_log.dto.request.auth.AuthenticateRequest;
import com.shongon.audit_log.dto.request.auth.IntrospectRequest;
import com.shongon.audit_log.dto.request.auth.LogoutRequest;
import com.shongon.audit_log.dto.request.auth.RefreshRequest;
import com.shongon.audit_log.dto.response.auth.AuthenticateResponse;
import com.shongon.audit_log.dto.response.auth.IntrospectResponse;
import com.shongon.audit_log.service.auth.AuthService;
import com.shongon.audit_log.utils.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthController {
    AuthService authService;

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) {
        return ApiResponse.<IntrospectResponse>builder()
                .code(200)
                .result(authService.introspect(introspectRequest))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest authRequest) {
        return ApiResponse.<AuthenticateResponse>builder()
                .code(200)
                .result(authService.authenticate(authRequest))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authService.logout(logoutRequest);
        return ApiResponse.<String>builder()
                .code(200)
                .result("Logout success")
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticateResponse> refreshToken(@RequestBody RefreshRequest refreshRequest)
            throws ParseException, JOSEException {

        return ApiResponse.<AuthenticateResponse>builder()
                .code(200)
                .result(authService.refreshToken(refreshRequest))
                .build();
    }
}

