package com.impulsofit.dto.response;

import java.time.LocalDateTime;

public record ComentarioResponseDTO (
    Long id,
    String contenido,
    String perfilNombre,
    LocalDateTime fechaCreacion
) {}