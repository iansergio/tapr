package com.example.authservice.infrastructure.persistence;

import com.example.authservice.domain.refresh.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataRefreshTokenJpa extends JpaRepository<RefreshToken, UUID> {

    @Query("SELECT t FROM RefreshToken t WHERE t.tokenHash.value = :hash AND t.revoked = false")
    Optional<RefreshToken> findActiveByTokenHash_Hash(@Param("hash") String hash);

    @Modifying
    @Transactional
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.id = :id")
    void revoke(@Param("id") UUID id);
}
