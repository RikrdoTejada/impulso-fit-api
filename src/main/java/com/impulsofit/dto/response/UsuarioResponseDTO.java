package com.impulsofit.dto.response;

import com.impulsofit.model.CodPregunta;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsuarioResponseDTO(
Long id,
String username,
String email,
Instant createdAt,
LocalDate fecha_Nacimiento,
String genero,
LocalDateTime fecha_creacion,
CodPregunta cod_pregunta
) {}
