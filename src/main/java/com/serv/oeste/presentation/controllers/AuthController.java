package com.serv.oeste.presentation.controllers;

import com.serv.oeste.application.dtos.AuthTokenPair;
import com.serv.oeste.application.dtos.reponses.AuthAccessTokenResponse;
import com.serv.oeste.application.dtos.requests.AuthLoginRequest;
import com.serv.oeste.application.dtos.requests.AuthRegisterRequest;
import com.serv.oeste.application.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @Value("${security.jwt.refresh-time}")
    private long refreshTokenValidTime;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody AuthRegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthAccessTokenResponse> login(@RequestBody AuthLoginRequest loginRequest, HttpServletResponse response) {
        AuthTokenPair tokens = authService.login(loginRequest);
        setRefreshCookie(response, tokens.refreshToken());
        return ResponseEntity.ok(new AuthAccessTokenResponse(tokens.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthAccessTokenResponse> refresh(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        String accessToken = authService.refresh(refreshToken);
        setRefreshCookie(response, accessToken);
        return ResponseEntity.ok(new AuthAccessTokenResponse(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        authService.logout(refreshToken);
        clearRefreshCookie(response);
        return ResponseEntity.accepted().build();
    }

    private void setRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMillis(refreshTokenValidTime))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private static void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
