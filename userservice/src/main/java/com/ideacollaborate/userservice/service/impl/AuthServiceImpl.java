package com.ideacollaborate.userservice.service.impl;

import com.ideacollaborate.userservice.dao.TokenDao;
import com.ideacollaborate.userservice.dao.UserDao;
import com.ideacollaborate.userservice.dto.request.AuthRequest;
import com.ideacollaborate.userservice.dto.request.TokenRequest;
import com.ideacollaborate.userservice.dto.response.TokenResponse;
import com.ideacollaborate.userservice.exception.ErrorCode;
import com.ideacollaborate.userservice.exception.auth.InvalidCredentialsException;
import com.ideacollaborate.userservice.exception.auth.InvalidTokenException;
import com.ideacollaborate.userservice.exception.user.UserAlreadyExistsException;
import com.ideacollaborate.userservice.exception.user.UserNotFoundException;
import com.ideacollaborate.userservice.model.Token;
import com.ideacollaborate.userservice.model.User;
import com.ideacollaborate.userservice.security.JwtService;
import com.ideacollaborate.userservice.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final TokenDao tokenDao;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(AuthRequest registerRequest) {

        if (userDao.existsByUserIdAndDeletedFalse(registerRequest.getUserId())) {
            throw new UserAlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = User.builder()
                .userId(registerRequest.getUserId())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userDao.save(user);
    }

    @Transactional
    public TokenResponse login (AuthRequest loginRequest) {

        User user = userDao.findByUserIdAndDeletedFalse(loginRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Token token = Token.builder()
                .userId(loginRequest.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        tokenDao.save(token);

        return TokenResponse.builder()
                .userId(loginRequest.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout (TokenRequest logoutRequest) {

        User user = userDao.findByUserIdAndDeletedFalse(logoutRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (jwtService.isTokenValid(logoutRequest.getAccessToken(), user)) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }

        tokenDao.deleteByUserId(logoutRequest.getUserId());
    }

    @Transactional
    public TokenResponse token(TokenRequest tokenRequest) {

        User user = userDao.findByUserIdAndDeletedFalse(tokenRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        Token token = tokenDao.findByUserId(tokenRequest.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!token.getRefreshToken().equals(tokenRequest.getRefreshToken())) {
            throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
        }

        String accessToken = jwtService.generateAccessToken(user);

        token.setAccessToken(accessToken);
        tokenDao.save(token);

        return TokenResponse.builder()
                .userId(tokenRequest.getUserId())
                .accessToken(accessToken)
                .refreshToken(tokenRequest.getRefreshToken())
                .build();
    }
}