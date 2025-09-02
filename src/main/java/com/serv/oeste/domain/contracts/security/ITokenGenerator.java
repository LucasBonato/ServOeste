package com.serv.oeste.domain.contracts.security;

import com.serv.oeste.domain.entities.user.User;

public interface ITokenGenerator {
    String generateAccessToken(User user);
}
