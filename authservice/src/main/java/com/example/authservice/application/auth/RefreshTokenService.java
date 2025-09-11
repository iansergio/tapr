package com.example.authservice.application.auth;

import org.springframework.transaction.annotation.Transactional;

import com.example.authservice.domain.user.User;
import org.springframework.stereotype.Service;

import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.RefreshTokenRepository;
import com.example.authservice.domain.refresh.vo.TokenHash;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void save(RefreshToken refreshToken) {
        User user = refreshToken.getUser();
        if (user != null && user.getId() != null) {
            refreshTokenRepository.findByUserId(
                    user.getId()).ifPresent(existingToken -> refreshTokenRepository.deleteById(existingToken.getId()));
        }
        refreshTokenRepository.save(refreshToken);
    }

    public User refreshTokens(String refreshTokenHash) {
        RefreshToken refreshToken = refreshTokenRepository.findActiveByHash(new TokenHash(refreshTokenHash))
                .filter(token -> !token.isRevoked() && !token.getExpiresAt().isExpired())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido ou expirado"));

        // Revoga o token antigo
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getUser();
    }

    public void revoke(RefreshToken refreshToken) {
        refreshTokenRepository.revoke(refreshToken.getId());
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.deleteById(refreshToken.getId());
    }
}
