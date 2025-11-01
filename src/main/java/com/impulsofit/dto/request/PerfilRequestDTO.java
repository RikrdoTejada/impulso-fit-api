package com.impulsofit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PerfilRequestDTO (
    @NotBlank(message = "El nombre de usuario es obligatorio")
    String nombre,
    String apellido,
    String biografia,
    String ubicacion,
    String fotoPerfil
){}

