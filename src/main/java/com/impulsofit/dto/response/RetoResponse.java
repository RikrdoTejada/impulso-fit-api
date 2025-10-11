package com.impulsofit.dto.response;

import java.time.LocalDate;

public record RetoResponse(
        Long id_reto,
        String grupoNombre,
    String usuarioNombre,
    String unidadNombre,
    String nombre,
    String descripcion,
    String objetivo,
    LocalDate fecha_inicio,
    LocalDate fecha_fin) {
}
