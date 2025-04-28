package com.shongon.mini_bank.config.jwt;

import com.nimbusds.jose.JOSEException;
import com.shongon.mini_bank.dto.request.auth.IntrospectRequest;
import com.shongon.mini_bank.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Autowired
    private AuthService authService;

    private NimbusJwtDecoder jwtDecoder = null;

    @Value("${jwt.signer-key}")
    private String signerKey;

    @Override
    public Jwt decode(String token) throws JwtException {
        var response = authService.introspect(
                IntrospectRequest.builder().token(token).build());
        if (!response.isValid()) throw new JwtException("Invalid token");

        if (Objects.isNull(jwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return jwtDecoder.decode(token);
    }
}
