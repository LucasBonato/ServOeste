package com.serv.oeste.infrastructure.configuration.dto;

public record JwtClaims(
        String username,
        String role,
        int userId
) { }
