package com.serv.oeste.infrastructure.entities.user;

import com.serv.oeste.infrastructure.security.RefreshToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "refresh_token", indexes = {
        @Index(columnList = "Username"),
        @Index(columnList = "Token_Hash")
})
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "Username", nullable = false, length = 100)
    private String username;

    @Column(name = "Token_Hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "Expires_At", nullable = false)
    private Instant expiresAt;

    @Column(name = "Revoked_At")
    private Instant revokedAt;

    @CreationTimestamp
    @Column(name = "Created_At")
    private Instant createdAt;


    public RefreshTokenEntity(RefreshToken refreshToken) {
        this.username = refreshToken.getUsername();
        this.tokenHash = refreshToken.getTokenHash();
        this.expiresAt = refreshToken.getExpiresAt();
        this.revokedAt = refreshToken.getRevokedAt();
    }

    public RefreshToken toRefreshToken() {
        return new RefreshToken(
            username,
            tokenHash,
            expiresAt,
            revokedAt
        );
    }
}
