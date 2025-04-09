package com.ideacollaborate.userservice.security;

import com.ideacollaborate.userservice.exception.ErrorCode;
import com.ideacollaborate.userservice.exception.GlobalExceptionHandler;
import com.ideacollaborate.userservice.exception.auth.InvalidTokenException;
import com.ideacollaborate.userservice.service.CustomUserDetailsService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private static final int BEARER_LENGTH = BEARER.length();
    private static final String AUTH = "Authorization";

    @Autowired
    JwtService jwtService;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired(required = false)
    private MeterRegistry metricsRegistry;

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        if (!jwtProperties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader(AUTH);

        trackAuthenticationAttempt();

        if (!isBearerPresent(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            authenticateIfValidToken(request, authorizationHeader);
        }
        catch (InvalidTokenException e) {
            trackAuthenticationFailure();
            globalExceptionHandler.handleAuthExceptions(e);
        }
    }

    private boolean isBearerPresent(String authorizationHeader) {
        return authorizationHeader != null && !authorizationHeader.isBlank() &&
                authorizationHeader.startsWith(BEARER);
    }

    private void authenticateIfValidToken(
            HttpServletRequest request, String authenticationHeader
    ) throws InvalidTokenException {

        String token = authenticationHeader.substring(BEARER_LENGTH);
        String username = jwtService.extractUsername(token);

        if (username != null && !userAlreadyAuthenticated(username)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                setSecurityContextAuthentication(request, userDetails);
                trackAuthenticationSuccess();
            }

            else {
                trackInvalidToken();
                throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
            }
        }
    }

    boolean userAlreadyAuthenticated(String username) {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getName().equals(username);
    }

    private void setSecurityContextAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void trackAuthenticationAttempt() {
        if (metricsRegistry != null) {
            metricsRegistry.counter("jwt.authentication.attempts").increment();
        }
    }

    private void trackAuthenticationSuccess() {
        if (metricsRegistry != null) {
            metricsRegistry.counter("jwt.authentication.success").increment();
        }
    }

    private void trackAuthenticationFailure() {
        if (metricsRegistry != null) {
            metricsRegistry.counter("jwt.authentication.failures").increment();
        }
    }

    private void trackInvalidToken() {
        if (metricsRegistry != null) {
            metricsRegistry.counter("jwt.authentication.invalid_tokens").increment();
        }
    }
}
