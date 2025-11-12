package com.impulsofit.dto.response;

import com.impulsofit.model.CodPregunta;
import java.time.LocalDateTime;

public record UsuarioResponseDTO(
    Long id_usuario,
    String email,
    LocalDateTime fecha_creacion,
    CodPregunta cod_pregunta
) {}
