package com.serv.oeste.domain.entities.user;

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

    public void setRevokedAt(Instant now) {
        this.revokedAt = now;
    }
}
