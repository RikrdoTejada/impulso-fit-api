package com.impulsofit.dto.request;

public record CreatePostRequest(
        Long userId,
        String content,
        Long challengeId,
        Long groupId
) {}