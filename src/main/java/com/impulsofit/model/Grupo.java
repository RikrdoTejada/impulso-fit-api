package com.impulsofit.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Long idGrupo;

    private String nombre;

    @OneToMany(mappedBy = "grupo")
    private List<PublicacionGrupo> publicaciones;

    // Getters y Setters
    public Long getIdGrupo() { return idGrupo; }
    public void setIdGrupo(Long idGrupo) { this.idGrupo = idGrupo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<PublicacionGrupo> getPublicaciones() { return publicaciones; }
    public void setPublicaciones(List<PublicacionGrupo> publicaciones) { this.publicaciones = publicaciones; }
}
