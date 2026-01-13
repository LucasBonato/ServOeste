package com.serv.oeste.application.contracts.repositories;

import com.serv.oeste.application.dtos.security.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    void revokeByTokenHash(String tokenHash);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
