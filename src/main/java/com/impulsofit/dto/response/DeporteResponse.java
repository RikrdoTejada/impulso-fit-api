package com.impulsofit.dto.response;

public record DeporteResponse(
        Long id_deporte,
        String nombre,
        String tipo_deporte
) {
}