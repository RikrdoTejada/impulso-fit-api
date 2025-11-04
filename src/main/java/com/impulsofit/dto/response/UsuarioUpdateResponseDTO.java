package com.impulsofit.dto.response;

import java.time.LocalDate;

public record UsuarioUpdateResponseDTO(
        Long id,
        String nombre,
        String apellido_p,
        String apellido_m,
        LocalDate fecha_nacimiento,
        String genero
) {
}
