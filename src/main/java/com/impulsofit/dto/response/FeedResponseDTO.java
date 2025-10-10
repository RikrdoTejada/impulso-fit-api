package com.impulsofit.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class FeedResponseDTO {
    private Long idPublicacion;
    private String nombreUsuario;
    private String contenido;
    private String nombreGrupo;
    private LocalDateTime fechaPublicacion;
    private List<ComentarioResponseDTO> comentarios;

    // Getters y setters
    public Long getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Long idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public List<ComentarioResponseDTO> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<ComentarioResponseDTO> comentarios) {
        this.comentarios = comentarios;
    }
}
