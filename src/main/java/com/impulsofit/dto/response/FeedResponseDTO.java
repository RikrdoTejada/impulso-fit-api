package com.impulsofit.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FeedResponseDTO (
    Long idPublicacion,
    String nombreUsuario,
    String contenido,
    String nombreGrupo,
    LocalDateTime fechaPublicacion,
    List<ComentarioResponseDTO> comentarios)
{}
