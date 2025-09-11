package com.example.authservice.application.auth;

import java.util.Optional;

import com.example.authservice.application.port.TokenService;
import com.example.authservice.domain.user.User;
import org.springframework.stereotype.Service;

import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.RefreshTokenRepository;
import com.example.authservice.domain.refresh.vo.TokenHash;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, TokenService tokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
    }

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public TokenService.TokenPair refreshTokens(String refreshTokenHash) {
        RefreshToken refreshToken = refreshTokenRepository.findActiveByHash(new TokenHash(refreshTokenHash))
                .filter(token -> !token.isRevoked() && !token.getExpiresAt().isExpired())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido ou expirado"));

        // Revoga o token antigo
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        // Emite outro token
        User user = refreshToken.getUser();
        TokenService.TokenPair newToken = tokenService.issue(user);
        refreshTokenRepository.save(new RefreshToken());
        
        return newToken;
    }

    public Optional<RefreshToken> findActiveByHash(String tokenHash) {
        return refreshTokenRepository.findActiveByHash(new TokenHash(tokenHash))
                .filter(token -> !token.isRevoked() && !token.getExpiresAt().isExpired());
    }

    public void revoke(RefreshToken refreshToken) {
        refreshTokenRepository.revoke(refreshToken.getId());
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.deleteById(refreshToken.getId());
    }
}
