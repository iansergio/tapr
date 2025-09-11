package com.example.authservice.infrastructure.persistence;

import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.RefreshTokenRepository;
import com.example.authservice.domain.refresh.vo.TokenHash;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class JpaRefreshTokenRepository implements RefreshTokenRepository {

    private SpringDataRefreshTokenJpa jpa;

    public JpaRefreshTokenRepository(SpringDataRefreshTokenJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<RefreshToken> findByUserId(UUID userId) {
        return jpa.findByUser_Id(userId);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return jpa.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findActiveByHash(TokenHash tokenHash) {
        return jpa.findByTokenHash(tokenHash);
    }

    @Override
    public void revoke(UUID id) {
        jpa.findById(id).ifPresent(token -> {
            token.setRevoked(true);
            jpa.save(token);
        });
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
        jpa.flush();
    }

}
