package com.serv.oeste.infrastructure.security;

import com.serv.oeste.application.exceptions.auth.AuthRefreshTokenRevokedException;
import com.serv.oeste.domain.contracts.repositories.IRefreshTokenRepository;
import com.serv.oeste.domain.contracts.security.IRefreshTokenStore;
import com.serv.oeste.domain.entities.user.RefreshToken;
import com.serv.oeste.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenStore {
    private final IRefreshTokenRepository refreshTokenRepository;

    @Value("${security.jwt.refresh-time}")
    private long refreshTokenValidTime;

    private final SecureRandom random = new SecureRandom();

    @Override
    public RefreshToken issue(User user) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        String tokenHash = sha256Hex(raw);

        RefreshToken refreshToken = new RefreshToken(
            user.getUsername(),
            tokenHash,
            Instant.now().plusMillis(refreshTokenValidTime),
            null
        );

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findValid(String token) {
        String tokenHash = sha256Hex(token);

        return refreshTokenRepository.findByTokenHash(tokenHash)
                .filter(refreshToken -> refreshToken.getRevokedAt() == null)
                .filter(refreshToken -> refreshToken.getExpiresAt().isAfter(Instant.now()));
    }

    @Override
    public void revoke(String token) {
        String tokenHash = sha256Hex(token);

        refreshTokenRepository.revoke(new RefreshToken(null, tokenHash, null, null));
    }

    private static String sha256Hex(String rawString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(messageDigest.digest(rawString.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException e) {
            throw new AuthRefreshTokenRevokedException();
        }
    }
}
