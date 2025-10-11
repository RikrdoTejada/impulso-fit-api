package com.impulsofit.dto.response;

import java.time.Instant;

public record PostResponse(
        Long id,
        Long userId,
        String content,
        Long challengeId,  // puede ser null
        Long groupId,      // SIEMPRE viene (la tabla lo exige)
        Instant createdAt
) {}