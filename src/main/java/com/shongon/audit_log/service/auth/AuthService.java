package com.shongon.audit_log.service.auth;

import com.nimbusds.jose.JOSEException;
import com.shongon.audit_log.dto.request.auth.AuthenticateRequest;
import com.shongon.audit_log.dto.request.auth.IntrospectRequest;
import com.shongon.audit_log.dto.request.auth.LogoutRequest;
import com.shongon.audit_log.dto.request.auth.RefreshRequest;
import com.shongon.audit_log.dto.response.auth.AuthenticateResponse;
import com.shongon.audit_log.dto.response.auth.IntrospectResponse;
import com.shongon.audit_log.exception.AppException;
import com.shongon.audit_log.exception.ErrorCode;
import com.shongon.audit_log.model.InvalidatedToken;
import com.shongon.audit_log.repository.InvalidatedTokenRepository;
import com.shongon.audit_log.repository.UserRepository;
import com.shongon.audit_log.service.security.AuditLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.ParseException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    InvalidatedTokenRepository tokenRepository;
    PasswordEncoder passwordEncoder;
    AuditLogService auditLogService;
    JwtService jwtService;

    @Transactional
    public AuthenticateResponse authenticate(AuthenticateRequest authRequest) {
        var user = userRepository
                .findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isAuthenticated = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        if (!isAuthenticated) throw new AppException(ErrorCode.LOGIN_FAILED);

        var token = jwtService.generateToken(user);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                auditLogService.logAfterCommit(
                        user.getUserId(),
                        "AUTHENTICATE",
                        "USER",
                        user.getUserId(),
                        "User authenticated: " + user.getUsername()
                );
            }
        });

        return AuthenticateResponse.builder()
                .isAuthenticated(true)
                .token(token)
                .build();
    }

    @Transactional
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        try {
            var signToken = jwtService.verifyToken(logoutRequest.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            var expTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expTime(expTime)
                    .build();

            tokenRepository.save(invalidatedToken);
        } catch (AppException e) {
            log.info("Token expired!");
        }
    }

    @Transactional
    public AuthenticateResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var checkToken = jwtService.verifyToken(request.getToken(), true);

        // Vô hiệu hóa token cũ
        var checkJit = checkToken.getJWTClaimsSet().getJWTID();
        var checkExpTime = checkToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(checkJit)
                .expTime(checkExpTime)
                .build();

        tokenRepository.save(invalidatedToken);

        // Tạo token mới
        var username = checkToken.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = jwtService.generateToken(user);

        return AuthenticateResponse.builder()
                .isAuthenticated(true)
                .token(token)
                .build();
    }

    @Transactional
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        var token = introspectRequest.getToken();
        boolean isValid = true;

        try {
            jwtService.verifyToken(token, false);
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }
}
