package com.impulsofit.model;

import jakarta.persistence.*;

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
}
