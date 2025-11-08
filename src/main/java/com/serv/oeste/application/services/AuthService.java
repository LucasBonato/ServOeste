package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.AuthTokenPair;
import com.serv.oeste.application.dtos.requests.AuthLoginRequest;
import com.serv.oeste.domain.contracts.repositories.IUserRepository;
import com.serv.oeste.domain.contracts.security.IRefreshTokenStore;
import com.serv.oeste.domain.contracts.security.ITokenGenerator;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.exceptions.auth.AuthInvalidCredentialsException;
import com.serv.oeste.domain.exceptions.auth.AuthRefreshTokenRevokedException;
import com.serv.oeste.infrastructure.security.IssuedRefreshToken;
import com.serv.oeste.infrastructure.security.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ITokenGenerator tokenGenerator;
    private final IRefreshTokenStore refreshTokenStore;

    public AuthTokenPair login(AuthLoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(AuthInvalidCredentialsException::new);

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new AuthInvalidCredentialsException();
        }

        String accessToken = tokenGenerator.generateAccessToken(user);
        IssuedRefreshToken issuedRefreshToken = refreshTokenStore.issue(user);
        return new AuthTokenPair(accessToken, issuedRefreshToken.rawToken());
    }

    public String refresh(String rawRefreshToken) {
        RefreshToken oldRefreshToken = refreshTokenStore.findValid(rawRefreshToken).orElseThrow(AuthRefreshTokenRevokedException::new);

        User user = userRepository.findByUsername(oldRefreshToken.getUsername())
                .orElseThrow(AuthInvalidCredentialsException::new);

        return tokenGenerator.generateAccessToken(user);
    }

    public void logout(String rawRefreshToken) {
        refreshTokenStore.findValid(rawRefreshToken)
                        .ifPresent(refreshToken -> refreshTokenStore.revoke(rawRefreshToken));
    }
}
