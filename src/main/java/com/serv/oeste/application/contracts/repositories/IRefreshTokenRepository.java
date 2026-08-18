package com.serv.oeste.application.contracts.repositories;

import com.serv.oeste.application.dtos.security.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    void revokeByUsername(String username);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
