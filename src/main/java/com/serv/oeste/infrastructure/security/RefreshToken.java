package com.serv.oeste.infrastructure.security;

import com.serv.oeste.domain.entities.user.User;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

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

    public static RawAndRefreshToken createFor(User user, Duration validFor) {
        String raw = UUID.randomUUID().toString();
        String tokenHash = HashUtils.sha256Hex(raw);
        Instant expiry = Instant.now().plus(validFor);
        RefreshToken refreshToken = new RefreshToken(
                user.getUsername(),
                tokenHash,
                expiry,
                null
        );
        return new RawAndRefreshToken(raw, refreshToken);
    }
}
