package com.serv.oeste.infrastructure.repositories.implementations;

import com.serv.oeste.domain.exceptions.auth.AuthNotValidException;
import com.serv.oeste.domain.contracts.repositories.IRefreshTokenRepository;
import com.serv.oeste.domain.entities.user.RefreshToken;
import com.serv.oeste.infrastructure.entities.user.RefreshTokenEntity;
import com.serv.oeste.infrastructure.repositories.jpa.IRefreshTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
    public void revoke(RefreshToken refreshToken) {
        if (refreshToken.getTokenHash() == null) {
            throw new AuthNotValidException("TokenHash is required to revoke the refresh token");
        }

        int rowsAffected = refreshTokenJpaRepository.revokeAllActiveForUser(Instant.now(), refreshToken.getTokenHash());

        if (rowsAffected == 0) {
            throw new AuthNotValidException("Could not revoke token");
        }
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHash(tokenHash).map(RefreshTokenEntity::toRefreshToken);
    }
}
