package com.impulsofit.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "grupo")
    private List<PublicacionGrupo> publicaciones;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<PublicacionGrupo> getPublicaciones() { return publicaciones; }
    public void setPublicaciones(List<PublicacionGrupo> publicaciones) { this.publicaciones = publicaciones; }
}

