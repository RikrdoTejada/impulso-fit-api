package com.impulsofit.dto.response;

public class GrupoResponseDTO {
    private Integer id;
    private String nombre;
    private String deporte;
    private String descripcion;
    private String accionUnirseUrl;

    public GrupoResponseDTO() {}

    public GrupoResponseDTO(Integer id, String nombre, String deporte, String descripcion, String accionUnirseUrl) {
        this.id = id;
        this.nombre = nombre;
        this.deporte = deporte;
        this.descripcion = descripcion;
        this.accionUnirseUrl = accionUnirseUrl;
    }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDeporte() { return deporte; }
    public String getDescripcion() { return descripcion; }
    public String getAccionUnirseUrl() { return accionUnirseUrl; }
}

