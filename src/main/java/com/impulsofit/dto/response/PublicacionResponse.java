package com.impulsofit.dto.response;

import java.time.LocalDateTime;

public record PublicacionResponse(
        Long id_publicaion,
        String usuarioNombre,
        Boolean publica,
        String grupoNombre,
        String contenido,
        LocalDateTime fecha_publicacion
) {
}
