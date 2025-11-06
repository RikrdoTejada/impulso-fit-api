package com.impulsofit.dto.response;

import java.time.LocalDate;

public record GrupoResponseDTO(
        Long id_grupo,
        String nombre,
        String deporte,
        String descripcion,
        String accionUnirseUrl,
        String ubicacion,
        LocalDate fecha_creacion
) {}