package com.ideacollaborate.userservice.controller;

import com.ideacollaborate.userservice.dto.request.AuthRequest;
import com.ideacollaborate.userservice.dto.request.TokenRequest;
import com.ideacollaborate.userservice.dto.response.TokenResponse;
import com.ideacollaborate.userservice.model.Token;
import com.ideacollaborate.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register (@RequestBody @Valid AuthRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login (@RequestBody @Valid AuthRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout (@RequestBody @Valid TokenRequest logoutRequest) {
        authService.logout(logoutRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> token (@RequestBody @Valid TokenRequest tokenRequest) {
        TokenResponse tokenResponse = authService.token(tokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}