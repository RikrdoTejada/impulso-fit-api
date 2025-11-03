package com.impulsofit.dto.response;

import com.impulsofit.model.CodPregunta;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id_usuario,
        String nombres,
        String apellido_p,
        String apellido_m,
        String email,
        String contrasena,
        LocalDate fecha_Nacimiento,
        String genero,
        LocalDateTime fecha_creacion,
        CodPregunta cod_pregunta
) {
}