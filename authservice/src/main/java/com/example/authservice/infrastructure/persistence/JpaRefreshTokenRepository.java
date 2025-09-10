package com.example.authservice.infrastructure.persistence;

import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.RefreshTokenRepository;
import com.example.authservice.domain.refresh.vo.TokenHash;

import java.util.Optional;
import java.util.UUID;

public class JpaRefreshTokenRepository implements RefreshTokenRepository {

    private SpringDataRefreshTokenJpa refreshTokenJpa;

    public JpaRefreshTokenRepository(SpringDataRefreshTokenJpa refreshTokenJpa) {
        this.refreshTokenJpa = refreshTokenJpa;
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJpa.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findActiveByHash(TokenHash tokenHash) {
        return refreshTokenJpa.findActiveByTokenHash_Hash(tokenHash.getValue());
    }

    @Override
    public void revoke(UUID id) {
        refreshTokenJpa.revoke(id);
    }

    @Override
    public void deleteById(UUID id) {
        refreshTokenJpa.deleteById(id);
    }
}
