package com.serv.oeste.domain.contracts.security;

import com.serv.oeste.infrastructure.security.RefreshToken;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.infrastructure.security.IssuedRefreshToken;

import java.util.Optional;

public interface IRefreshTokenStore {
    IssuedRefreshToken issue(User user);
    Optional<RefreshToken> findValid(String token);
    void revoke(String rawToken);
}
