package com.impulsofit.dto.response;

import com.impulsofit.model.PublicacionType;

import java.time.LocalDateTime;

public record PublicacionResponseDTO(
        Long id_publicacion,
        String usuarioNombre,
        PublicacionType tipo_publicacion,
        String grupoNombre,
        String contenido,
        LocalDateTime fecha_publicacion
) {
}