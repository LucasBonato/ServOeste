package com.serv.oeste.infrastructure.middleware;

import com.serv.oeste.domain.exceptions.auth.AuthTokenExpiredException;
import com.serv.oeste.domain.exceptions.auth.AuthTokenNotValidException;
import com.serv.oeste.infrastructure.configuration.dto.JwtClaims;
import com.serv.oeste.infrastructure.security.contracts.ITokenVerifier;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.opentelemetry.api.trace.Span;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final ITokenVerifier tokenVerifier;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/auth/login")
                || path.equals("/auth/refresh")
                || path.equals("/swagger")
                || path.equals("/docs")
                || path.equals("/scalar")
                || path.startsWith("/scalar/")
                || path.startsWith("/v3/api-docs/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.substring(7);

        try {
            final JwtClaims claims = tokenVerifier.verify(accessToken);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(claims.username(), null, List.of(new SimpleGrantedAuthority(claims.role())));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                Span.current()
                        .setAttribute("user.name", claims.username())
                        .setAttribute("user.id", claims.userId());
            }
        }
        catch (ExpiredJwtException e) {
            handlerExceptionResolver.resolveException(
                    request, response, null, new AuthTokenExpiredException()
            );
            return;
        }
        catch (JwtException e) {
            handlerExceptionResolver.resolveException(
                    request, response, null, new AuthTokenNotValidException()
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}
