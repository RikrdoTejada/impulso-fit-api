package com.impulsofit.dto.response;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        String password,
        LocalDateTime createdAt
) {}