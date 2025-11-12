package com.impulsofit.dto.request;

public record PublicacionRequestDTO(
        Long id_perfil,
        Long id_grupo,
        String contenido
) {
}