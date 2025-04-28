package com.shongon.mini_bank.service.auth;

import com.nimbusds.jose.JOSEException;
import com.shongon.mini_bank.dto.request.auth.AuthenticateRequest;
import com.shongon.mini_bank.dto.request.auth.IntrospectRequest;
import com.shongon.mini_bank.dto.request.auth.LogoutRequest;
import com.shongon.mini_bank.dto.request.auth.RefreshRequest;
import com.shongon.mini_bank.dto.response.auth.AuthenticateResponse;
import com.shongon.mini_bank.dto.response.auth.IntrospectResponse;
import com.shongon.mini_bank.exception.AppException;
import com.shongon.mini_bank.exception.ErrorCode;
import com.shongon.mini_bank.model.InvalidatedToken;
import com.shongon.mini_bank.repository.InvalidatedTokenRepository;
import com.shongon.mini_bank.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    InvalidatedTokenRepository tokenRepository;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;

    @Transactional
    public AuthenticateResponse authenticate(AuthenticateRequest authRequest) {
        var user = userRepository
                .findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isAuthenticated = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        if (!isAuthenticated) throw new AppException(ErrorCode.LOGIN_FAILED);

        var token = jwtService.generateToken(user);

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
