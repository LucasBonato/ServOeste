package com.serv.oeste.application.services;

import com.serv.oeste.application.dtos.AuthTokenPair;
import com.serv.oeste.application.dtos.requests.AuthLoginRequest;
import com.serv.oeste.application.dtos.requests.AuthRegisterRequest;
import com.serv.oeste.application.exceptions.auth.AuthInvalidCredentialsException;
import com.serv.oeste.application.exceptions.auth.AuthNotValidException;
import com.serv.oeste.application.exceptions.auth.AuthRefreshTokenRevokedException;
import com.serv.oeste.domain.contracts.repositories.IUserRepository;
import com.serv.oeste.domain.contracts.security.IRefreshTokenStore;
import com.serv.oeste.domain.contracts.security.ITokenGenerator;
import com.serv.oeste.domain.entities.user.RefreshToken;
import com.serv.oeste.domain.entities.user.User;
import com.serv.oeste.domain.enums.Roles;
import com.serv.oeste.infrastructure.security.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final ITokenGenerator tokenGenerator;
    private final IRefreshTokenStore refreshTokenStore;

    public ResponseEntity<Void> register(AuthRegisterRequest registerRequest) {
        if (registerRequest.role() == Roles.ADMIN)
            throw new AuthNotValidException("Invalid register, cannot register an ADMIN user");

        if (userRepository.findByUsername(registerRequest.username()).isPresent())
            throw new AuthNotValidException("Username is already in use");

        userRepository.save(new User(
            registerRequest.username(),
            passwordEncoder.encode(registerRequest.password()),
            registerRequest.role()
        ));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    public AuthTokenPair login(AuthLoginRequest loginRequest) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());

        if (!passwordEncoder.matches(loginRequest.password(), userDetails.getPassword())) {
            throw new AuthInvalidCredentialsException();
        }

        User user = new User(
            userDetails.getUsername(),
            userDetails.getPassword(),
            Roles.valueOf(authorityToRole(userDetails.getAuthorities()))
        );

        String accessToken = tokenGenerator.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenStore.issue(user);

        return new AuthTokenPair(accessToken, refreshToken.getTokenHash());
    }

    public String refresh(String refreshTokenHash) {
        RefreshToken refreshToken = refreshTokenStore.findValid(refreshTokenHash).orElseThrow(AuthRefreshTokenRevokedException::new);
        UserDetails user = userDetailsService.loadUserByUsername(refreshToken.getUsername());
        return tokenGenerator.generateAccessToken(new User(
            user.getUsername(),
            null,
            Roles.valueOf(authorityToRole(user.getAuthorities()))
        ));
    }

    public void logout(String refreshToken) {
        refreshTokenStore.revoke(refreshToken);
    }

    private static String authorityToRole(Collection<? extends GrantedAuthority> auths) {
        return auths.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority -> authority.substring("ROLE_".length()))
                .findFirst()
                .orElse(Roles.TECHNICIAN.getRole());
    }
}
