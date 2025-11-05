package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1500)
    private String contenido;

    @Column(nullable = false)
    private Instant createdAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reto_id")
    private Reto reto;

    public Publicacion() {}

    // getters
    public Long getId() { return id; }

    public String getContenido() { return contenido; }

    public Instant getCreatedAt() { return createdAt; }

    public Usuario getUsuario() { return usuario; }

    public Grupo getGrupo() { return grupo; }

    public Reto getReto() { return reto; }

    // setters
    public void setId(Long id) { this.id = id; }

    public void setContenido(String contenido) { this.contenido = contenido; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    public void setReto(Reto reto) { this.reto = reto; }
}