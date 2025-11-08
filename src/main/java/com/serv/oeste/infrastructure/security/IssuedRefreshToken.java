package com.serv.oeste.infrastructure.security;

public record IssuedRefreshToken(String rawToken, RefreshToken stored) { }
