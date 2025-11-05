package com.impulsofit.dto.response;

public record EstadisticaRetoResponseDTO(
    Long idUsuario,
    String nombreUsuario,
    Long diasCompletados,
    Long totalDias,
    Double porcentajeCumplimiento,
    Integer puntosTotales,
    Integer ranking)
{}