package com.impulsofit.dto.response;

import java.time.LocalDate;

public record UsuarioResponse(
        Long id_usuario,
        String nombre,
        String email,
        String contrasena,
        Integer edad,
        String genero,
        LocalDate fecha_creacion
) {
}
