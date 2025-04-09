package com.ideacollaborate.userservice.service;

import com.ideacollaborate.userservice.dto.request.UserRequest;
import com.ideacollaborate.userservice.dto.response.UserResponse;
import com.ideacollaborate.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<UserResponse> getAllUsers();
    public UserResponse getUserById(String userId);
    public UserResponse updateUser(String userId, UserRequest userUpdateRequest);
    public void deleteUser(String userId);
}
