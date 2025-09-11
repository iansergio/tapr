package com.example.authservice.domain.refresh;

import com.example.authservice.domain.refresh.vo.TokenHash;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findActiveByHash(TokenHash tokenHash);

    Optional<RefreshToken> findByUserId(UUID userId);

    void revoke(UUID id);

    void deleteById(UUID id);
}
