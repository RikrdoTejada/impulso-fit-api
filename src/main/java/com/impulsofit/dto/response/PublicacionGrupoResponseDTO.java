package com.impulsofit.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record PublicacionGrupoResponseDTO(
    Long id,
    String contenido,
    String autor,
    LocalDateTime fechaCreacion,
    List<String> comentarios
) {}
