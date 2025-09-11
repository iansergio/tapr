package com.example.authservice.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.authservice.application.auth.RefreshTokenService;
import com.example.authservice.application.port.TokenService;
import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.vo.ExpiresAt;
import com.example.authservice.domain.refresh.vo.TokenHash;
import com.example.authservice.domain.user.User;
import com.example.authservice.infrastructure.config.JwtProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {
    private final JwtProperties props;
    private final RefreshTokenService refreshTokenService;

    @Override
    public TokenPair issue(User user) {
        if (props.getSecret() == null || props.getSecret().isBlank()) {
            throw new IllegalStateException("jwt.secret deve ser definido");
        }

        Instant now = Instant.now();
        Algorithm alg = Algorithm.HMAC256(props.getSecret().getBytes(StandardCharsets.UTF_8));

        // Access token
        Instant accessExp = now.plusSeconds(props.getAccessTtlSeconds());
        String access = JWT.create()
                .withIssuer(props.getIssuer())
                .withAudience(props.getAudience())
                .withSubject(user.getId().toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(accessExp))
                .withClaim("type", "access")
                .withClaim("email", user.getEmail().getValue())
                .withClaim("role", user.getRole().getValue().name())
                .withClaim("level", user.getRole().getValue().getLevel())
                .sign(alg);

        // Refresh token
        Instant refreshExp = now.plusSeconds(props.getRefresTtlSeconds());
        String refreshToken = JWT.create()
                .withIssuer(props.getIssuer())
                .withAudience(props.getAudience())
                .withSubject(user.getId().toString())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(refreshExp))
                .withClaim("type", "refresh")
                .withClaim("email", user.getEmail().getValue())
                .withClaim("role", user.getRole().getValue().name())
                .withClaim("level", user.getRole().getValue().getLevel())
                .sign(alg);

        String refreshTokenHash = hashToken(refreshToken);

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setTokenHash(new TokenHash(refreshTokenHash));
        refreshEntity.setExpiresAt(ExpiresAt.of(refreshExp));
        refreshEntity.setRevoked(false);
        refreshEntity.setUser(user);
        refreshTokenService.save(refreshEntity);

        return new TokenPair(access, refreshToken, props.getRefresTtlSeconds());
    }

    private String hashToken(String token) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar o hash do token", e);
        }
    }
}
