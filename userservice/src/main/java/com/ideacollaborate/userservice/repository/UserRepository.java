package com.ideacollaborate.userservice.repository;

import com.ideacollaborate.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserIdAndDeletedFalse(String userId);
    Optional<User> findByUserIdAndDeletedFalse(String userId);
    void deleteByUserIdAndDeletedFalse(String userId);
    List<User> findAllByDeletedFalse();
}
