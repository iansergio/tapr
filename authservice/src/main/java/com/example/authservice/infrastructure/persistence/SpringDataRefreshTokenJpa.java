package com.example.authservice.infrastructure.persistence;

import com.example.authservice.domain.refresh.RefreshToken;
import com.example.authservice.domain.refresh.vo.TokenHash;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataRefreshTokenJpa extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(TokenHash tokenHash);

    Optional<RefreshToken> findByUser_Id(UUID userId);
}
