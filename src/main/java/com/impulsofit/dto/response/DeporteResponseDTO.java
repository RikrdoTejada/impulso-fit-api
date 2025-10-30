package com.impulsofit.dto.response;

public class DeporteResponseDTO {
    private Integer idDeporte;
    private String nombre;
    private String tipoDeporte;

    public DeporteResponseDTO() {}

    public DeporteResponseDTO(Integer idDeporte, String nombre, String tipoDeporte) {
        this.idDeporte = idDeporte;
        this.nombre = nombre;
        this.tipoDeporte = tipoDeporte;
    }

    public Integer getIdDeporte() { return idDeporte; }
    public String getNombre() { return nombre; }
    public String getTipoDeporte() { return tipoDeporte; }
}

