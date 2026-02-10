package com.serv.oeste.infrastructure.security;

import com.serv.oeste.application.contracts.repositories.IRefreshTokenRepository;
import com.serv.oeste.application.contracts.security.IRefreshTokenStore;
import com.serv.oeste.application.dtos.security.IssuedRefreshToken;
import com.serv.oeste.application.dtos.security.RawAndRefreshToken;
import com.serv.oeste.application.dtos.security.RefreshToken;
import com.serv.oeste.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenStore {
    @Value("${security.jwt.refresh-time}")
    private long refreshTokenValidTime;

    private final IRefreshTokenRepository refreshTokenRepository;

    private RawAndRefreshToken createFor(User user, Duration validFor) {
        String raw = UUID.randomUUID().toString();
        String tokenHash = HashUtils.sha256Hex(raw);

        RefreshToken refreshToken = new RefreshToken(
                user.getUsername(),
                tokenHash,
                Instant.now().plus(validFor),
                null
        );

        return new RawAndRefreshToken(raw, refreshToken);
    }

    @Override
    public IssuedRefreshToken issue(User user) {
        RawAndRefreshToken pair = createFor(user, Duration.ofMillis(refreshTokenValidTime));
        RefreshToken savedRefreshToken = refreshTokenRepository.save(pair.refreshToken());
        return new IssuedRefreshToken(pair.raw(), savedRefreshToken);
    }

    @Override
    public Optional<RefreshToken> findValid(String rawRefreshToken) {
        String tokenHash = HashUtils.sha256Hex(rawRefreshToken);
        return refreshTokenRepository.findByTokenHash(tokenHash)
                .filter(refreshToken -> !refreshToken.isRevoked() && !refreshToken.isExpired());
    }

    @Override
    public void revoke(String rawToken) {
        String tokenHash = HashUtils.sha256Hex(rawToken);
        refreshTokenRepository.revokeByTokenHash(tokenHash);
    }
}
