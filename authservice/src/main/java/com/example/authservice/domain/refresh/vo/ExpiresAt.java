package com.example.authservice.domain.refresh.vo;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
public class ExpiresAt {

    private Instant expiresAt;

    private ExpiresAt(Instant expiresAt) {
        this.expiresAt = Objects.requireNonNull(expiresAt);
    }

    public static ExpiresAt of(Instant instant) {
        return new ExpiresAt(instant);
    }

    public static ExpiresAt generateWithDays(int days) {
        return new ExpiresAt(Instant.now().plus(days, ChronoUnit.DAYS));
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }
}
