package com.serv.oeste.application.dtos.security;

public record AuthTokenPair(String accessToken, String rawRefreshToken) { }
