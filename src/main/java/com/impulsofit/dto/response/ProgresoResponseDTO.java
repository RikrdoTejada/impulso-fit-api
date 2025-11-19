package com.impulsofit.dto.response;

public record ProgresoResponseDTO(
    Long idPerfil,
    Long idReto,
    Double total,
    Double porcentaje
) {}
