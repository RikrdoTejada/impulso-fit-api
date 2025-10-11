package com.impulsofit.dto.request;

public record UserRequest(
        String username,
        String email,
        String password
) {}