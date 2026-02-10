package com.serv.oeste.application.contracts.security;

import com.serv.oeste.application.dtos.security.RefreshToken;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.application.dtos.security.IssuedRefreshToken;

import java.util.Optional;

public interface IRefreshTokenStore {
    IssuedRefreshToken issue(User user);
    Optional<RefreshToken> findValid(String token);
    void revoke(String rawToken);
}
