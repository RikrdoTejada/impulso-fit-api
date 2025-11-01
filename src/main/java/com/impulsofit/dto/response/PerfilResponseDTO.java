package com.impulsofit.dto.response;

public record PerfilResponseDTO(
    Long idPerfil,
    String nombre,
    String apellido,
    String biografia,
    String ubicacion,
    String fotoPerfil
) {}
