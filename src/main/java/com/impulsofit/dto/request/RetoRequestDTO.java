package com.impulsofit.dto.request;

import java.time.LocalDate;

public record RetoRequestDTO(
        Long id_grupo,
        Long id_perfil_creador,
        Long id_unidad,
        String titulo,
        String descripcion,
        Double objetivo_total,
        LocalDate fecha_inicio,
        LocalDate fecha_fin){
}