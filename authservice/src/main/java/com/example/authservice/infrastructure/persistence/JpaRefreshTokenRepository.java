package com.example.authservice.infrastructure.persistence;

import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.RefreshTokenRepository;
import com.example.authservice.domain.refresh.vo.TokenHash;

import java.util.Optional;
import java.util.UUID;

public class JpaRefreshTokenRepository implements RefreshTokenRepository {

    private SpringDataRefreshTokenJpa jpa;

    public JpaRefreshTokenRepository(SpringDataRefreshTokenJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return jpa.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findActiveByHash(TokenHash tokenHash) {
        return jpa.findByTokenHash(tokenHash)
                .filter(token -> !token.isRevoked() && !token.getExpiresAt().isExpired());
    }

    @Override
    public void revoke(UUID id) {
        jpa.findById(id).ifPresent(refreshToken -> {
            refreshToken.setRevoked(true);
            jpa.save(refreshToken);
        });
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
