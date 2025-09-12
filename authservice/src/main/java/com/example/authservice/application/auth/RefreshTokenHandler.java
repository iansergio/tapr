package com.example.authservice.application.auth;

import com.example.authservice.application.port.TokenService;
import com.example.authservice.interfaces.rest.dto.auth.TokenResponse;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenHandler {
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    public RefreshTokenHandler(RefreshTokenService refreshTokenService, TokenService tokenService) {
        this.refreshTokenService = refreshTokenService;
        this.tokenService = tokenService;
    }

    /**
     * Valida, revoga o refresh token antigo, gera novos tokens e retorna o DTO.
     */
    public TokenResponse handle(String refreshTokenHash) {
        var user = refreshTokenService.revokeAndGetUser(refreshTokenHash);
        var pair = tokenService.issue(user);
        return new TokenResponse(pair.accessToken(), pair.refreshToken(), pair.expiresInSeconds());
    }
}
