package com.impulsofit.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class PublicacionGrupoResponseDTO {
    private Long id;
    private String contenido;
    private String autor;
    private LocalDateTime fechaCreacion;
    private List<String> comentarios;

    public PublicacionGrupoResponseDTO(Long id, String contenido, String autor, LocalDateTime fechaCreacion, List<String> comentarios) {
        this.id = id;
        this.contenido = contenido;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.comentarios = comentarios;
    }

    public Long getId() { return id; }
    public String getContenido() { return contenido; }
    public String getAutor() { return autor; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public List<String> getComentarios() { return comentarios; }
}
