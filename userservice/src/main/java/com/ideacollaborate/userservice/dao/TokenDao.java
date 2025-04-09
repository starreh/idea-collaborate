package com.ideacollaborate.userservice.dao;

import com.ideacollaborate.userservice.model.Token;
import com.ideacollaborate.userservice.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TokenDao {

    @Autowired
    TokenRepository tokenRepository;

    public boolean existsByUserId(String userId) {
        return tokenRepository.existsByUserId(userId);
    }

    public Optional<Token> findByUserId(String userId) {
        return tokenRepository.findByUserId(userId);
    }

    public void deleteByUserId(String userId) {
        tokenRepository.deleteByUserId(userId);
    }

    public Token save(Token token) {
        return tokenRepository.save(token);
    }
}
