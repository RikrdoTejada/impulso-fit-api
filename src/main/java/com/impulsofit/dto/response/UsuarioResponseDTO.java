package com.impulsofit.dto.response;

import java.time.Instant;

public record UsuarioResponseDTO(
        Long id,
        String username,
        String email,
        Instant createdAt
) {}