package com.impulsofit.dto.response;

import java.time.Instant;

public record SeguidorResponseDTO(
        Long id,
        Long followerId,
        Long followingId,
        Instant createdAt
) { }