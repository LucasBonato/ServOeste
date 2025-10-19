package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.AuthTokenPair;
import com.serv.oeste.application.dtos.requests.AuthLoginRequest;
import com.serv.oeste.application.dtos.requests.AuthRegisterRequest;
import com.serv.oeste.domain.contracts.repositories.IUserRepository;
import com.serv.oeste.domain.contracts.security.IRefreshTokenStore;
import com.serv.oeste.domain.contracts.security.ITokenGenerator;
import com.serv.oeste.infrastructure.security.RefreshToken;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.enums.Roles;
import com.serv.oeste.domain.exceptions.auth.AuthInvalidCredentialsException;
import com.serv.oeste.domain.exceptions.auth.AuthNotValidException;
import com.serv.oeste.domain.exceptions.auth.AuthRefreshTokenRevokedException;
import com.serv.oeste.infrastructure.security.IssuedRefreshToken;
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

    public void register(AuthRegisterRequest registerRequest) {
        if (registerRequest.role() == Roles.ADMIN)
            throw new AuthNotValidException("Cadastro inválido, não é possível registrar um usuário ADMIN");

        if (userRepository.findByUsername(registerRequest.username()).isPresent())
            throw new AuthNotValidException("Nome de usuário já está em uso");

        userRepository.save(new User(
            registerRequest.username(),
            passwordEncoder.encode(registerRequest.password()),
            registerRequest.role()
        ));
    }

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
//        refreshTokenStore.revoke(rawRefreshToken);

        User user = userRepository.findByUsername(oldRefreshToken.getUsername())
                .orElseThrow(AuthInvalidCredentialsException::new);

        String accessToken = tokenGenerator.generateAccessToken(user);

//        IssuedRefreshToken newRefreshToken = refreshTokenStore.issue(user);

        return accessToken;
    }

    public void logout(String rawRefreshToken) {
        refreshTokenStore.findValid(rawRefreshToken)
                        .ifPresent(refreshToken -> refreshTokenStore.revoke(rawRefreshToken));
    }
}
