package com.shongon.mini_bank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(500, HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized Error"),

    PERMISSION_EXISTED(409, HttpStatus.CONFLICT, "Permission already existed"),
    PERMISSION_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Permission not found"),

    ROLE_EXISTED(409, HttpStatus.CONFLICT, "Role already existed"),
    ROLE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Role not found"),

    LOGIN_FAILED(401, HttpStatus.UNAUTHORIZED, "Username or password is incorrect"),
    INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED, "Invalid/Expired Token"),
    UNAUTHENTICATED(401, HttpStatus.UNAUTHORIZED, "Unauthenticated"),

    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "User not found"),

    ;


    ErrorCode(int code, HttpStatusCode statusCode, String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;
}
