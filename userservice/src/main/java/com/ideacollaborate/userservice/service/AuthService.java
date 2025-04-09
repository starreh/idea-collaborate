package com.ideacollaborate.userservice.service;

import com.ideacollaborate.userservice.dto.request.AuthRequest;
import com.ideacollaborate.userservice.dto.request.TokenRequest;
import com.ideacollaborate.userservice.dto.response.TokenResponse;

public interface AuthService {
    void register(AuthRequest registerRequest);
    TokenResponse login(AuthRequest loginRequest);
    void logout(TokenRequest logoutRequest);
    TokenResponse token(TokenRequest tokenRequest);
}