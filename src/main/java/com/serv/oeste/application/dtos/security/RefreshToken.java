package com.serv.oeste.application.dtos.security;

import java.time.Instant;

public class RefreshToken {
    private final String username;
    private final String tokenHash;
    private final Instant expiresAt;
    private Instant revokedAt;

    public RefreshToken(String username, String tokenHash, Instant expiresAt, Instant revokedAt) {
        this.username = username;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.revokedAt = revokedAt;
    }

    public void revoke() {
        this.revokedAt = Instant.now();
    }

    public boolean isRevoked() {
        return revokedAt != null;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }

    public String getUsername() {
        return username;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }
}
