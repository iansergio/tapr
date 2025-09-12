package com.example.authservice.application.auth;

import org.springframework.transaction.annotation.Transactional;

import com.example.authservice.domain.user.User;
import org.springframework.stereotype.Service;

import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.RefreshTokenRepository;
import com.example.authservice.domain.refresh.vo.TokenHash;
import com.example.authservice.application.port.TokenService;
import com.example.authservice.interfaces.rest.dto.auth.TokenResponse;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, TokenService tokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
    }
    
    // Valida, revoga o refresh token antigo e gera novos tokens e retorna o dto.
    public TokenResponse refreshAndIssueNewTokens(String refreshTokenHash) {
        RefreshToken refreshToken = refreshTokenRepository.findActiveByHash(new TokenHash(refreshTokenHash))
                .filter(token -> !token.isRevoked() && !token.getExpiresAt().isExpired())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido ou expirado"));

        // Revoga o token antigo
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        // Emite novos tokens
        TokenService.TokenPair pair = tokenService.issue(refreshToken.getUser());
        return new TokenResponse(pair.accessToken(), pair.refreshToken(), pair.expiresInSeconds());
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
