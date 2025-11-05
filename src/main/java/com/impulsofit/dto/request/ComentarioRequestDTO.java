package com.impulsofit.dto.request;

public record ComentarioRequestDTO(
    String contenido,
    Long usuarioId,
    Long publicacionId
) {}
