package com.impulsofit.dto.request;

import com.impulsofit.model.CodPregunta;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UsuarioRequestDTO(
        String nombres,
        String apellido_p,
        String apellido_m,
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato de email inválido")
        String email,
        String contrasena,
        LocalDate fecha_nacimiento,
        String genero,
        CodPregunta cod_pregunta
) {
}