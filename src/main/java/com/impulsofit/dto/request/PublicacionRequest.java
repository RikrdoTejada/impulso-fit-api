package com.impulsofit.dto.request;

public record PublicacionRequest(
        Long id_usuario,
        Long id_grupo,
        String contenido
) {
}