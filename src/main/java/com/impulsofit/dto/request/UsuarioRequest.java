package com.impulsofit.dto.request;

import java.time.LocalDate;

public record UsuarioRequest(
        String nombre,
        String email,
        String contrasena,
        Integer edad,
        String genero,
        LocalDate fecha_registro
) {
}
