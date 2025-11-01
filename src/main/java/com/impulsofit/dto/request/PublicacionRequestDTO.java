package com.impulsofit.dto.request;

public record PublicacionRequestDTO(
        Long id_usuario,
        Long id_grupo,
        String contenido
) {
}