package com.impulsofit.dto.response;

import java.time.LocalDate;

public record RetoResponse(
    Long id_reto,
    String grupoNombre,
    String creadorNombre,
    String unidad,
    String nombre,
    String descripcion,
    String objetivo,
    LocalDate fechaPublicacion,
    LocalDate fecha_inicio,
    LocalDate fecha_fin) {
}
