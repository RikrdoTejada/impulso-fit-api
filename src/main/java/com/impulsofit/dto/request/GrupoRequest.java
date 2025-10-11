package com.impulsofit.dto.request;

import java.time.LocalDate;

public record GrupoRequest(
        Long id_usuario_creador,
        Long id_deporte,
        String nombre,
        String descripcion,
        String ubicacion,
        LocalDate fecha_creacion
) {
}
