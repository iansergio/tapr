package com.example.authservice.interfaces.rest;

import com.example.authservice.application.auth.PasswordLoginHandler;
import com.example.authservice.application.auth.RefreshTokenHandler;
import com.example.authservice.interfaces.rest.dto.auth.LogoutRequest;
import com.example.authservice.interfaces.rest.dto.auth.PasswordLoginRequest;
import com.example.authservice.interfaces.rest.dto.auth.TokenResponse;
import com.example.authservice.interfaces.rest.dto.auth.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final PasswordLoginHandler passwordLoginHandler;
    private final RefreshTokenHandler refreshTokenHandler;

    @Operation(summary = "Login e emissão de tokens")
    @PostMapping("/login/password")
    public ResponseEntity<TokenResponse> loginWithPassword(@Valid @RequestBody PasswordLoginRequest request) {
        TokenResponse response = passwordLoginHandler.handle(request.email(), request.password());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Refresh de tokens")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshTokens(@Valid @RequestBody RefreshTokenRequest request) {
        TokenResponse response = refreshTokenHandler.handle(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logout do usuário e revogação do refresh token")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        refreshTokenHandler.handle(request.refreshToken()); // revoga e ignora o retorno
        return ResponseEntity.noContent().build();
    }

}
