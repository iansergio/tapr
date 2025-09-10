package com.example.authservice.domain.refresh.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpiresAt {

    private Instant expiresAt;

}
