package com.impulsofit.dto.request;

import com.impulsofit.model.RecoverType;
import jakarta.validation.constraints.Pattern;

public record RecoverRequestDTO(
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato de email inválido")
        String email,
        String respuesta,
        RecoverType tipo_recover,
        String new_contrasena,
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Formato de email inválido")
        String new_email
) {
}