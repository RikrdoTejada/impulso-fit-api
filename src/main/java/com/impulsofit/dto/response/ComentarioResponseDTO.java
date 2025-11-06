package com.impulsofit.dto.response;

import java.time.LocalDateTime;

public record ComentarioResponseDTO(
        String nombreUsuario,
        String contenido,
        LocalDateTime fechaComentario)
{}
