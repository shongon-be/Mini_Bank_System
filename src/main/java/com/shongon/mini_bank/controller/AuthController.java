package com.shongon.mini_bank.controller;



import java.text.ParseException;

import com.shongon.mini_bank.dto.request.auth.AuthenticateRequest;
import com.shongon.mini_bank.dto.request.auth.IntrospectRequest;
import com.shongon.mini_bank.dto.request.auth.LogoutRequest;
import com.shongon.mini_bank.dto.request.auth.RefreshRequest;
import com.shongon.mini_bank.dto.response.auth.AuthenticateResponse;
import com.shongon.mini_bank.dto.response.auth.IntrospectResponse;
import com.shongon.mini_bank.service.auth.AuthService;
import com.shongon.mini_bank.utils.ApiResponse;
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
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {

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
    public ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {

        authService.logout(logoutRequest);

        return ApiResponse.<Void>builder().code(200).build();
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

