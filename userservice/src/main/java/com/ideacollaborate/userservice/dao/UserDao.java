package com.ideacollaborate.userservice.dao;

import com.ideacollaborate.userservice.model.User;
import com.ideacollaborate.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    @Autowired
    UserRepository userRepository;

    public boolean existsByUserIdAndDeletedFalse(String userId) {
        return userRepository.existsByUserIdAndDeletedFalse(userId);
    }

    public Optional<User> findByUserIdAndDeletedFalse(String userId) {
        return userRepository.findByUserIdAndDeletedFalse(userId);
    }

    public void deleteByUserIdAndDeletedFalse(String userId) {
        userRepository.deleteByUserIdAndDeletedFalse(userId);
    }

    public List<User> findAllByDeletedFalse() {
        return userRepository.findAllByDeletedFalse();
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
