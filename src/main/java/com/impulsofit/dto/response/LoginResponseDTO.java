package com.impulsofit.dto.response;

public record LoginResponseDTO(
    Long idUsuario,
    String nombre,
    String email,
    Integer edad,
    String genero
) {}
