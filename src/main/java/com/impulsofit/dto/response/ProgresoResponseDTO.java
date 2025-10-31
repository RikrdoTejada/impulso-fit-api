package com.impulsofit.dto.response;

public class ProgresoResponseDTO {
    private Long idUsuario;
    private Long idReto;
    private Double total;
    private Double porcentaje;

    public ProgresoResponseDTO(Long idUsuario, Long idReto, Double total, Double porcentaje) {
        this.idUsuario = idUsuario;
        this.idReto = idReto;
        this.total = total;
        this.porcentaje = porcentaje;
    }

    public Long getIdUsuario() { return idUsuario; }
    public Long getIdReto() { return idReto; }
    public Double getTotal() { return total; }
    public Double getPorcentaje() { return porcentaje; }
}