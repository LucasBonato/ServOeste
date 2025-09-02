package com.serv.oeste.infrastructure.security;

import com.serv.oeste.domain.contracts.security.ITokenGenerator;
import com.serv.oeste.domain.contracts.security.ITokenVerifier;
import com.serv.oeste.domain.entities.user.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenService implements ITokenGenerator, ITokenVerifier {
    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.valid-time}")
    private long accessTokenValidTime;

    private SecretKey key() { return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); }

    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().getRoleWithPrefix())
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(Duration.ofMillis(accessTokenValidTime))))
                .signWith(key())
                .compact();
    }

    @Override
    public boolean isValid(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .toInstant().isAfter(Instant.now());
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .toInstant().isBefore(Instant.now());
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
