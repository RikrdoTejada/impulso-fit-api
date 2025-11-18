package com.impulsofit.dto.request;

import jakarta.validation.constraints.NotBlank;


public record PerfilRequestDTO (
    @NotBlank(message = "El nombre de perfil es obligatorio")
    String nombre_perfil,
    String biografia,
    String ubicacion,
    String foto_perfil
){}
