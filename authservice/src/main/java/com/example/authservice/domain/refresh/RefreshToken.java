package com.example.authservice.domain.refresh;

import com.example.authservice.domain.refresh.vo.ExpiresAt;
import com.example.authservice.domain.refresh.vo.TokenHash;
import com.example.authservice.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @Column(unique = true)
    private TokenHash tokenHash;

    @Embedded
    private ExpiresAt expiresAt;

    private boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
