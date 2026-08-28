package com.serv.oeste.infrastructure.security;

import com.serv.oeste.application.contracts.security.ITokenGenerator;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.infrastructure.configuration.dto.JwtClaims;
import com.serv.oeste.infrastructure.security.contracts.ITokenVerifier;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenService implements ITokenGenerator, ITokenVerifier {
    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.valid-time}")
    private long accessTokenValidTime;

    private SecretKey key;

    @PostConstruct
    private void initKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 256 bits (32 bytes)");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(accessTokenValidTime);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getRole().getRoleWithPrefix())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    @Override
    public JwtClaims verify(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return new JwtClaims(
                claims.getSubject(),
                claims.get("role", String.class),
                claims.get("userId", Integer.class)
        );
    }
}
