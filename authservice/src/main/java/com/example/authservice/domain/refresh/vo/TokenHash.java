package com.example.authservice.domain.refresh.vo;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class TokenHash {

    private String hash;

    public TokenHash(String hash) {
        this.hash = Objects.requireNonNull(hash);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TokenHash tokenHash = (TokenHash) o;
        return Objects.equals(hash, tokenHash.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hash);
    }
}
