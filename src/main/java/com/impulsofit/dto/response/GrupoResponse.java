package com.impulsofit.dto.response;

import java.time.LocalDate;

public record GrupoResponse (
        Long id_grupo,
        String usuarioNombre,
        String deporteNombre,
        String nombre,
        String descripcion,
        String ubicacion,
        LocalDate fecha_creacion
) {
}