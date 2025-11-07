package com.impulsofit.dto.response;

import java.time.LocalDate;

public record RetoResponseDTO(
    Long id_reto,
    String grupoNombre,
    String creadorNombre,
    String unidad,
    String titulo,
    String descripcion,
    Double objetivoTotal,
    LocalDate fechaPublicacion,
    LocalDate fecha_inicio,
    LocalDate fecha_fin
) {}
