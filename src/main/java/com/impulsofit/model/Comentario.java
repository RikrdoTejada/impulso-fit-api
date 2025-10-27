package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long id;

    @Column(name = "contenido", nullable = false, length = 500)
    private String contenido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private PublicacionGeneral publicacion;

    @Column(name = "fecha_comentario", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Constructor vacío
    public Comentario() {}

    // Constructor con campos
    public Comentario(String contenido, Usuario usuario, PublicacionGeneral publicacion) {
        this.contenido = contenido;
        this.usuario = usuario;
        this.publicacion = publicacion;
    }

    // Getters y setters
    public Long getId() { return id; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public PublicacionGeneral getPublicacion() { return publicacion; }
    public void setPublicacion(PublicacionGeneral publicacion) { this.publicacion = publicacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
