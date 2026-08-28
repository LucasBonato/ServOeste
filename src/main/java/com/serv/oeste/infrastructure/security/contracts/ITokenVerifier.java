package com.serv.oeste.infrastructure.security.contracts;

import com.serv.oeste.infrastructure.configuration.dto.JwtClaims;

public interface ITokenVerifier {
    JwtClaims verify(String token);
}
