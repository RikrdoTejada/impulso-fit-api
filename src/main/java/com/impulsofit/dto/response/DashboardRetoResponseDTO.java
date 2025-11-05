package com.impulsofit.dto.response;

import java.util.List;

public record DashboardRetoResponseDTO (
        boolean esParticipante,
        boolean tieneEstadisticas,
        String mensaje,
        List<EstadisticaRetoResponseDTO> estadisticasParticipantes,
        EstadisticaRetoResponseDTO miEstadistica,
        Integer miRanking,
        Long totalParticipantes
){}