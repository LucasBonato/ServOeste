package com.serv.oeste.infrastructure.security.contracts;

import org.springframework.security.core.userdetails.UserDetails;

public interface ITokenVerifier {
    boolean isValid(String token);
    boolean isTokenValidForUser(String token, UserDetails user);
    String extractUsername(String token);
}
