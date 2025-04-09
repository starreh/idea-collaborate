package com.ideacollaborate.userservice.service.impl;

import com.ideacollaborate.userservice.dao.UserDao;
import com.ideacollaborate.userservice.dto.request.UserRequest;
import com.ideacollaborate.userservice.dto.response.UserResponse;
import com.ideacollaborate.userservice.exception.ErrorCode;
import com.ideacollaborate.userservice.exception.user.UserNotFoundException;
import com.ideacollaborate.userservice.model.User;
import com.ideacollaborate.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private final PasswordEncoder passwordEncoder;


    public List<UserResponse> getAllUsers () {
        List<User> users = userDao.findAllByDeletedFalse();
        return users.stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    public UserResponse getUserById(String userId) {
        User user = userDao.findByUserIdAndDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        return mapToUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserRequest userUpdateRequest) {
        User user = userDao.findByUserIdAndDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        userDao.save(user);

        return mapToUserResponse(user);
    }

    public void deleteUser(String userId) {

        if(userDao.existsByUserIdAndDeletedFalse(userId)) {
            throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
        }

        userDao.deleteByUserIdAndDeletedFalse(userId);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .build();
    }
}
