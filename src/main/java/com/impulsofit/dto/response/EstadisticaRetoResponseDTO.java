package com.impulsofit.dto.response;

public class EstadisticaRetoResponseDTO {
    private Long idUsuario;
    private String nombreUsuario;
    private Long diasCompletados;
    private Long totalDias;
    private Double porcentajeCumplimiento;
    private Integer puntosTotales;
    private Integer ranking;

    // Constructores
    public EstadisticaRetoResponseDTO() {}

    public EstadisticaRetoResponseDTO(Long idUsuario, String nombreUsuario, Long diasCompletados, Long totalDias, Integer puntosTotales) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.diasCompletados = diasCompletados;
        this.totalDias = totalDias;
        this.puntosTotales = puntosTotales;
        this.porcentajeCumplimiento = totalDias > 0 ? (diasCompletados * 100.0) / totalDias : 0.0;
    }

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public Long getDiasCompletados() { return diasCompletados; }
    public void setDiasCompletados(Long diasCompletados) { this.diasCompletados = diasCompletados; }

    public Long getTotalDias() { return totalDias; }
    public void setTotalDias(Long totalDias) { this.totalDias = totalDias; }

    public Double getPorcentajeCumplimiento() { return porcentajeCumplimiento; }
    public void setPorcentajeCumplimiento(Double porcentajeCumplimiento) { this.porcentajeCumplimiento = porcentajeCumplimiento; }

    public Integer getPuntosTotales() { return puntosTotales; }
    public void setPuntosTotales(Integer puntosTotales) { this.puntosTotales = puntosTotales; }

    public Integer getRanking() { return ranking; }
    public void setRanking(Integer ranking) { this.ranking = ranking; }
}