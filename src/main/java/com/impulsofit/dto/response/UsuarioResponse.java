package com.impulsofit.dto.response;

import com.impulsofit.model.CodPregunta;

import java.time.LocalDate;

public record UsuarioResponse(
        Long id_usuario,
        String nombres,
        String apellido_p,
        String apellido_m,
        String email,
        String contrasena,
        LocalDate fecha_Nacimiento,
        String genero,
        LocalDate fecha_creacion,
        CodPregunta cod_pregunta
) {
}