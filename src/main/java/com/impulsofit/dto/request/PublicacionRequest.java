package com.impulsofit.dto.request;

import java.time.LocalDateTime;

public record PublicacionRequest(
        Long id_usuario,
        Long id_grupo,
        String contenido,
        LocalDateTime fecha_publicacion
) {
}
