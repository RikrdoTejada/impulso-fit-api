package com.impulsofit.dto.response;

import java.util.List;

public class DashboardRetoResponseDTO {
    private boolean esParticipante;
    private boolean tieneEstadisticas;
    private String mensaje;
    private List<EstadisticaRetoResponseDTO> estadisticasParticipantes;
    private EstadisticaRetoResponseDTO miEstadistica;
    private Integer miRanking;
    private Long totalParticipantes;

    // Constructores
    public DashboardRetoResponseDTO() {}

    public DashboardRetoResponseDTO(boolean esParticipante, boolean tieneEstadisticas, String mensaje) {
        this.esParticipante = esParticipante;
        this.tieneEstadisticas = tieneEstadisticas;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public boolean isEsParticipante() { return esParticipante; }
    public void setEsParticipante(boolean esParticipante) { this.esParticipante = esParticipante; }

    public boolean isTieneEstadisticas() { return tieneEstadisticas; }
    public void setTieneEstadisticas(boolean tieneEstadisticas) { this.tieneEstadisticas = tieneEstadisticas; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public List<EstadisticaRetoResponseDTO> getEstadisticasParticipantes() { return estadisticasParticipantes; }
    public void setEstadisticasParticipantes(List<EstadisticaRetoResponseDTO> estadisticasParticipantes) { this.estadisticasParticipantes = estadisticasParticipantes; }

    public EstadisticaRetoResponseDTO getMiEstadistica() { return miEstadistica; }
    public void setMiEstadistica(EstadisticaRetoResponseDTO miEstadistica) { this.miEstadistica = miEstadistica; }

    public Integer getMiRanking() { return miRanking; }
    public void setMiRanking(Integer miRanking) { this.miRanking = miRanking; }

    public Long getTotalParticipantes() { return totalParticipantes; }
    public void setTotalParticipantes(Long totalParticipantes) { this.totalParticipantes = totalParticipantes; }
}