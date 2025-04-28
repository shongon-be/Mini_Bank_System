package com.shongon.mini_bank.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shongon.mini_bank.exception.ErrorCode;
import com.shongon.mini_bank.utils.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType("application/json");

        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));

        response.flushBuffer();
    }
}
