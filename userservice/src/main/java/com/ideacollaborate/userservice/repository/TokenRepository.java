package com.ideacollaborate.userservice.repository;

import com.ideacollaborate.userservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    boolean existsByUserId(String userId);
    Optional<Token> findByUserId(String userId);
    void deleteByUserId(String userId);
}
