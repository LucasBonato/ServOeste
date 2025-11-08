package com.serv.oeste.presentation.controllers;

import com.serv.oeste.application.dtos.AuthTokenPair;
import com.serv.oeste.application.dtos.reponses.AuthAccessTokenResponse;
import com.serv.oeste.application.dtos.requests.AuthLoginRequest;
import com.serv.oeste.application.services.AuthService;
import com.serv.oeste.presentation.swagger.AuthSwagger;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthSwagger {
    @Value("${security.jwt.refresh-time}")
    private long refreshTokenValidTime;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthAccessTokenResponse> login(@RequestBody AuthLoginRequest loginRequest, HttpServletResponse response) {
        AuthTokenPair tokens = authService.login(loginRequest);
        setRefreshCookie(response, tokens.rawRefreshToken());
        return ResponseEntity.ok(new AuthAccessTokenResponse(tokens.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthAccessTokenResponse> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isBlank())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String accessToken = authService.refresh(refreshToken);

        return ResponseEntity.ok(new AuthAccessTokenResponse(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken != null && !refreshToken.isBlank())
            authService.logout(refreshToken);

        clearRefreshCookie(response);
        return ResponseEntity.accepted().build();
    }

    private void setRefreshCookie(HttpServletResponse response, String rawRefreshToken) {
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", rawRefreshToken)
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
