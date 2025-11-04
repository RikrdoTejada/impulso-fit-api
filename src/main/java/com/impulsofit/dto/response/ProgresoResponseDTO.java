package com.impulsofit.dto.response;

public record ProgresoResponseDTO(
    Long idUsuario,
    Long idReto,
    Double total,
    Double porcentaje
) {}
