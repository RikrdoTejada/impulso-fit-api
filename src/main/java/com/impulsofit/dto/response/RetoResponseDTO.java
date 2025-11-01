package com.impulsofit.dto.response;

import java.time.LocalDate;

public class RetoResponseDTO {
    private Long id_reto;
    private String grupoNombre;
    private String creadorNombre;
    private String unidad;
    private String nombre;
    private String descripcion;
    private String objetivo;
    private LocalDate fechaPublicacion;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;

    public RetoResponseDTO() {}

    public RetoResponseDTO(Long id, String titulo, String descripcion, String detalleUrl) {
        this.id_reto = id;
        this.nombre = titulo;
        this.descripcion = descripcion;
    }

    public RetoResponseDTO(Long id_reto, String grupoNombre, String creadorNombre, String unidad, String nombre, String descripcion, String objetivo, LocalDate fechaPublicacion, LocalDate fecha_inicio, LocalDate fecha_fin) {
        this.id_reto = id_reto;
        this.grupoNombre = grupoNombre;
        this.creadorNombre = creadorNombre;
        this.unidad = unidad;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.objetivo = objetivo;
        this.fechaPublicacion = fechaPublicacion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }

    // Getters / Setters
    public Long getId_reto() { return id_reto; }
    public void setId_reto(Long id_reto) { this.id_reto = id_reto; }

    public Long getId() { return id_reto; }

    public String getGrupoNombre() { return grupoNombre; }
    public void setGrupoNombre(String grupoNombre) { this.grupoNombre = grupoNombre; }

    public String getCreadorNombre() { return creadorNombre; }
    public void setCreadorNombre(String creadorNombre) { this.creadorNombre = creadorNombre; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public LocalDate getFecha_inicio() { return fecha_inicio; }
    public void setFecha_inicio(LocalDate fecha_inicio) { this.fecha_inicio = fecha_inicio; }

    public LocalDate getFecha_fin() { return fecha_fin; }
    public void setFecha_fin(LocalDate fecha_fin) { this.fecha_fin = fecha_fin; }
}
