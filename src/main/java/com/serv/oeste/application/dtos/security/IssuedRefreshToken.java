package com.serv.oeste.application.dtos.security;

public record IssuedRefreshToken(String rawToken, RefreshToken stored) { }
