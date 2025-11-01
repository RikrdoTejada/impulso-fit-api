package com.impulsofit.dto.response;

public class DeporteResponseDTO {
    private Long id_deporte;
    private String nombre;
    private String tipo_deporte;

    public DeporteResponseDTO() {}

    public DeporteResponseDTO(Long idDeporte, String nombre, String tipoDeporte) {
        this.id_deporte = idDeporte;
        this.nombre = nombre;
        this.tipo_deporte = tipoDeporte;
    }

    public Long getIdDeporte() { return id_deporte; }
    public String getNombre() { return nombre; }
    public String getTipoDeporte() { return tipo_deporte; }
}

