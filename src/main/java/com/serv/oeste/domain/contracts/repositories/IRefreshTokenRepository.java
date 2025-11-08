package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.infrastructure.security.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    void revokeByTokenHash(String tokenHash);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
