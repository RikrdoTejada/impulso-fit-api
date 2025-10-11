package com.impulsofit.dto.response;

import java.time.Instant;

public record FollowResponse(
        Long id,
        Long followerId,
        Long followingId,
        Instant createdAt
) { }