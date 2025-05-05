package com.shongon.mini_bank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED(500, HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized Error"),
    INVALID_KEY(400, HttpStatus.BAD_REQUEST, "Invalid Message Key"),

    USER_EXISTED(409, HttpStatus.CONFLICT, "User already existed"),
    USER_NOT_FOUND(404, HttpStatus.NOT_FOUND, "User not found"),

    INVALID_USERNAME(400, HttpStatus.BAD_REQUEST, "Username must be at least {min} characters"),
    INVALID_PASSWORD(400, HttpStatus.BAD_REQUEST, "Password must be at least {min} characters"),
    INVALID_DOB(400, HttpStatus.BAD_REQUEST, "Your age must be at least {min} years old"),

    LOGIN_FAILED(401, HttpStatus.UNAUTHORIZED, "Username or password is incorrect"),
    INVALID_TOKEN(401, HttpStatus.UNAUTHORIZED, "Invalid/Expired Token"),
    INVALID_OLD_PASSWORD(400, HttpStatus.BAD_REQUEST, "Old password is incorrect"),
    UNAUTHENTICATED(401, HttpStatus.UNAUTHORIZED, "Unauthenticated"),
    INVALID_PERMISSION(403, HttpStatus.FORBIDDEN, "You do not have permission to access this resource"),

    PERMISSION_EXISTED(409, HttpStatus.CONFLICT, "Permission already existed"),
    PERMISSION_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Permission not found"),

    ROLE_EXISTED(409, HttpStatus.CONFLICT, "Role already existed"),
    ROLE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "Role not found"),

    USER_LOGS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "No logs found for user with ID: {userId}"),
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
