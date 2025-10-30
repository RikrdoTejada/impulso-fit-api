package com.impulsofit.dto.response;

import java.time.LocalDateTime;

public class ComentarioResponseDTO {
    private Long id;
    private String contenido;
    private String nombreUsuario;
    private LocalDateTime fechaCreacion;

    public ComentarioResponseDTO(Long id, String contenido, String nombreUsuario, LocalDateTime fechaCreacion) {
        this.id = id;
        this.contenido = contenido;
        this.nombreUsuario = nombreUsuario;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters
    public Long getId() { return id; }
    public String getContenido() { return contenido; }
    public String getNombreUsuario() { return nombreUsuario; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
