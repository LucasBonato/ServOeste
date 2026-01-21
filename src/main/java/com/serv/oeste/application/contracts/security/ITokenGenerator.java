package com.serv.oeste.application.contracts.security;

import com.serv.oeste.domain.entities.user.User;

public interface ITokenGenerator {
    String generateAccessToken(User user);
}
