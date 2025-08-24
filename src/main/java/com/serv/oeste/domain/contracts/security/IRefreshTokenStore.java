package com.serv.oeste.domain.contracts.security;

import com.serv.oeste.domain.entities.user.RefreshToken;
import com.serv.oeste.domain.entities.user.User;

import java.util.Optional;

public interface IRefreshTokenStore {
    RefreshToken issue(User user);
    Optional<RefreshToken> findValid(String token);
    void revoke(String token);
}
