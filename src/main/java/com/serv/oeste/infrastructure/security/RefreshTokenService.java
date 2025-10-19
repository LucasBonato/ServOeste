package com.serv.oeste.infrastructure.security;

import com.serv.oeste.domain.contracts.repositories.IRefreshTokenRepository;
import com.serv.oeste.domain.contracts.security.IRefreshTokenStore;
import com.serv.oeste.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenStore {
    @Value("${security.jwt.refresh-time}")
    private long refreshTokenValidTime;

    private final IRefreshTokenRepository refreshTokenRepository;

    @Override
    public IssuedRefreshToken issue(User user) {
        RawAndRefreshToken pair = RefreshToken.createFor(user, Duration.ofMillis(refreshTokenValidTime));
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
