package com.impulsofit.dto.request;

public record CrearPublicacionRequestDTO(
        Long userId,
        String content,
        Long challengeId,
        Long groupId
) {}