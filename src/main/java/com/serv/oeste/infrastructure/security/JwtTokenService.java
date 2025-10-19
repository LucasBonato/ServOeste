package com.serv.oeste.infrastructure.security;

import com.serv.oeste.domain.contracts.security.ITokenGenerator;
import com.serv.oeste.infrastructure.security.contracts.ITokenVerifier;
import com.serv.oeste.domain.entities.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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
                .claim("role", user.getRole().getRoleWithPrefix())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean isValid(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims
                    .getExpiration()
                    .toInstant().isAfter(Instant.now());
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean isTokenValidForUser(String token, UserDetails user) {
        String username = extractUsername(token);
        return username != null && username.equals(user.getUsername()) && isValid(token);
    }

    @Override
    public String extractUsername(String token) {
        try {
            return parseClaims(token).getSubject();
        }
        catch (JwtException e) {
            return null;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
