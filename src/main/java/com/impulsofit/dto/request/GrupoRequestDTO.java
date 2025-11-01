package com.impulsofit.dto.request;

public record GrupoRequestDTO(
        Long id_usuario_creador,
        Long id_deporte,
        String nombre,
        String descripcion,
        String ubicacion
) {
}