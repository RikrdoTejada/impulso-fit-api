package com.impulsofit.dto.response;

import com.impulsofit.model.PublicacionType;

import java.time.Instant;
import java.time.LocalDateTime;

public record PublicacionResponseDTO(
        Long id,
        Long userId,
        String content,
        Long challengeId,  // puede ser null
        Long groupId,      // SIEMPRE viene (la tabla lo exige)
        Instant createdAt,
        Long id_publicacion,
        String usuarioNombre,
        PublicacionType tipo_publicacion,
        String grupoNombre,
        String contenido,
        LocalDateTime fecha_publicacion
) {}