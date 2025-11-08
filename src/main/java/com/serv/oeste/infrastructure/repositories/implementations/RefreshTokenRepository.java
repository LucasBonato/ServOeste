package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.contracts.repositories.IRefreshTokenRepository;
import com.serv.oeste.infrastructure.security.RefreshToken;
import com.serv.oeste.infrastructure.entities.user.RefreshTokenEntity;
import com.serv.oeste.infrastructure.repositories.jpa.IRefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepository implements IRefreshTokenRepository {
    private final IRefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenJpaRepository.save(new RefreshTokenEntity(refreshToken)).toRefreshToken();
    }

    @Override
    @Transactional
    public void revokeByTokenHash(String tokenHash) {
        refreshTokenJpaRepository.revokeAllActiveForUser(tokenHash, Instant.now());
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHash(tokenHash).map(RefreshTokenEntity::toRefreshToken);
    }
}
