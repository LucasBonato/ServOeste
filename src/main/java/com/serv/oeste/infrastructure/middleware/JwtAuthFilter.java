package com.serv.oeste.infrastructure.middleware;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.serv.oeste.domain.exceptions.auth.AuthTokenExpiredException;
import com.serv.oeste.domain.exceptions.auth.AuthTokenNotValidException;
import com.serv.oeste.infrastructure.security.contracts.ITokenVerifier;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final ITokenVerifier tokenVerifier;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.substring(7);

        try {
            if (!tokenVerifier.isValid(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String username = tokenVerifier.extractUsername(accessToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (tokenVerifier.isTokenValidForUser(accessToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
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
