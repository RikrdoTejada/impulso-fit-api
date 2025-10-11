package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenido;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "publicacion_grupo_id")
    private PublicacionGrupo publicacionGrupo;

    public Comentario() {}

    public Comentario(String contenido, Usuario usuario, PublicacionGrupo publicacionGrupo) {
        this.contenido = contenido;
        this.usuario = usuario;
        this.publicacionGrupo = publicacionGrupo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public PublicacionGrupo getPublicacionGrupo() { return publicacionGrupo; }
    public void setPublicacionGrupo(PublicacionGrupo publicacionGrupo) { this.publicacionGrupo = publicacionGrupo; }
}
