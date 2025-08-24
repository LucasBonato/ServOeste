package com.serv.oeste.domain.contracts.repositories;

import com.serv.oeste.domain.entities.user.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    void revoke(RefreshToken refreshToken);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
