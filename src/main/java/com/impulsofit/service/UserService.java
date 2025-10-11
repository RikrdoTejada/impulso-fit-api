package com.impulsofit.service;

import com.impulsofit.dto.request.UserRequest;
import com.impulsofit.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest u);
    UserResponse update(Long id, UserRequest u);      // <-- NUEVO
    void delete(Long id);
    UserResponse getById(Long id);
    List<UserResponse> list();
}