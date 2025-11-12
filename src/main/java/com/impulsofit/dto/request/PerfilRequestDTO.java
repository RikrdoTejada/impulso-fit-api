package com.impulsofit.dto.request;

import com.impulsofit.model.Persona;
import jakarta.validation.constraints.NotBlank;


public record PerfilRequestDTO (
    @NotBlank(message = "El nombre de perfil es obligatorio")
    String nombre_perfil,
    Long id_persona,
    String biografia,
    String ubicacion,
    String foto_perfil
){}

