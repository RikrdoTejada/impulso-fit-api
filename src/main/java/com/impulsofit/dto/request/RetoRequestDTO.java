package com.impulsofit.dto.request;

import java.time.LocalDate;

public record RetoRequestDTO(
        Long id_grupo,
        Long id_usuario_creador,
        Long id_unidad,
        String nombre,
        String descripcion,
        String objetivo,
        LocalDate fecha_inicio,
        LocalDate fecha_fin){
}