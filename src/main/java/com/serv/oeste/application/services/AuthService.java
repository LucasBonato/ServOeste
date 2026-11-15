package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.security.AuthTokenPair;
import com.serv.oeste.application.dtos.requests.AuthLoginRequest;
import com.serv.oeste.domain.contracts.repositories.IUserRepository;
import com.serv.oeste.application.contracts.security.IRefreshTokenStore;
import com.serv.oeste.application.contracts.security.ITokenGenerator;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.exceptions.auth.AuthInvalidCredentialsException;
import com.serv.oeste.domain.exceptions.auth.AuthRefreshTokenRevokedException;
import com.serv.oeste.application.dtos.security.IssuedRefreshToken;
import com.serv.oeste.application.dtos.security.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ITokenGenerator tokenGenerator;
    private final IRefreshTokenStore refreshTokenStore;

    public AuthTokenPair login(AuthLoginRequest loginRequest) {
        log.info("auth.login.attempt username={}", loginRequest.username());

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> {
                    log.warn("auth.login.invalid-credentials username={}", loginRequest.username());
                    return new AuthInvalidCredentialsException();
                });

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            log.warn("auth.login.invalid-credentials username={}", loginRequest.username());
            throw new AuthInvalidCredentialsException();
        }

        String accessToken = tokenGenerator.generateAccessToken(user);

        log.info("auth.login.revoke-tokens.started username={}", user.getUsername());
        refreshTokenStore.revoke(user.getUsername());
        log.info("auth.login.revoke-tokens.completed username={}", user.getUsername());

        IssuedRefreshToken issuedRefreshToken = refreshTokenStore.issue(user);

        log.info("auth.login.succeeded username={} roles={}", user.getUsername(), user.getRole());

        return new AuthTokenPair(accessToken, issuedRefreshToken.rawToken());
    }

    public String refresh(String rawRefreshToken) {
        log.info("auth.refresh.attempt");

        RefreshToken oldRefreshToken = refreshTokenStore.findValid(rawRefreshToken)
                .orElseThrow(() -> {
                    log.warn("auth.refresh.revoked-or-not-found");
                    return new AuthRefreshTokenRevokedException();
                });

        User user = userRepository.findByUsername(oldRefreshToken.getUsername())
                .orElseThrow(() -> {
                    log.warn("auth.refresh.user-not-found username={}", oldRefreshToken.getUsername());
                    return new AuthInvalidCredentialsException();
                });

        log.info("auth.refresh.succeeded username={}", user.getUsername());

        return tokenGenerator.generateAccessToken(user);
    }

    public void logout(String rawRefreshToken) {
        log.info("auth.logout.started");

        refreshTokenStore.findValid(rawRefreshToken)
                .ifPresent(refreshToken -> {
                    refreshTokenStore.revoke(refreshToken.getUsername());
                    log.info("auth.logout.completed username={}", refreshToken.getUsername());
                });
    }
}
