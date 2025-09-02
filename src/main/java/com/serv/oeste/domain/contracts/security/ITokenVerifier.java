package com.serv.oeste.domain.contracts.security;

public interface ITokenVerifier {
    boolean isValid(String token);
    boolean isExpired(String token);
    String extractUsername(String token);
}
