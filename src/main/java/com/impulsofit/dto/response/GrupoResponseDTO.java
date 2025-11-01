package com.impulsofit.dto.response;

import java.time.LocalDate;

public class GrupoResponseDTO {
    private Long id_grupo;
    private String nombre;
    private String deporte;
    private String descripcion;
    private String accionUnirseUrl;
    private String ubicacion;
    private LocalDate fecha_creacion;

    public GrupoResponseDTO(Integer id, String nombre, String deporte, String descripcion, String accionUnirseUrl) {
        this.id_grupo = (id == null) ? null : id.longValue();
        this.nombre = nombre;
        this.deporte = deporte;
        this.descripcion = descripcion;
        this.accionUnirseUrl = accionUnirseUrl;
    }

    public GrupoResponseDTO(Long id_grupo, String nombre, String deporte, String descripcion, String accionUnirseUrl) {
        this.id_grupo = id_grupo;
        this.nombre = nombre;
        this.deporte = deporte;
        this.descripcion = descripcion;
        this.accionUnirseUrl = accionUnirseUrl;
    }

    public GrupoResponseDTO(Long id_grupo, String nombre, String deporte, String descripcion, String ubicacion, LocalDate fecha_creacion) {
        this.id_grupo = id_grupo;
        this.nombre = nombre;
        this.deporte = deporte;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fecha_creacion = fecha_creacion;
    }

    public Integer getId() {
        return (id_grupo == null) ? null : id_grupo.intValue();
    }

    public Long getId_grupo() { return id_grupo; }
    public void setId_grupo(Long id_grupo) { this.id_grupo = id_grupo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDeporte() { return deporte; }
    public void setDeporte(String deporte) { this.deporte = deporte; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getAccionUnirseUrl() { return accionUnirseUrl; }
    public void setAccionUnirseUrl(String accionUnirseUrl) { this.accionUnirseUrl = accionUnirseUrl; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public LocalDate getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(LocalDate fecha_creacion) { this.fecha_creacion = fecha_creacion; }
}
