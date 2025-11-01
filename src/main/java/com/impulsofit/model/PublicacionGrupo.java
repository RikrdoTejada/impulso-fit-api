package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "publicaciongrupo")
@PrimaryKeyJoinColumn(name = "id_publicacion")
public class PublicacionGrupo extends PublicacionGeneral {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    public PublicacionGrupo() {}

    public PublicacionGrupo(String contenido, Usuario usuario, Grupo grupo) {
        super(contenido, usuario);
        this.grupo = grupo;
    }

    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    // Métodos delegados para compatibilidad con la versión de la rama destino
    // (evitan duplicar campos mapeados en la superclase PublicacionGeneral)
    public Long getIdPublicacion() { return super.getId(); }
    public void setIdPublicacion(Long id) { super.setId(id); }

    @Override
    public String getContenido() { return super.getContenido(); }
    @Override
    public void setContenido(String contenido) { super.setContenido(contenido); }

    public Usuario getAutor() { return super.getUsuario(); }
    public void setAutor(Usuario autor) { super.setUsuario(autor); }

    // Asegurar fechaCreacion si no está seteada
    @PrePersist
    protected void onCreate() {
        if (super.getFechaCreacion() == null) {
            super.setFechaCreacion(LocalDateTime.now());
        }
    }

    public LocalDateTime getFechaCreacion() { return super.getFechaCreacion(); }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { super.setFechaCreacion(fechaCreacion); }
}