package com.impulsofit.model;

import java.io.Serializable;
import java.util.Objects;

public class ParticipacionRetoId implements Serializable {
    private Long reto;
    private Long usuario;

    // Constructores, getters, setters
    public ParticipacionRetoId() {}

    public ParticipacionRetoId(Long reto, Long usuario) {
        this.reto = reto;
        this.usuario = usuario;
    }

    public Long getReto() { return reto; }
    public void setReto(Long reto) { this.reto = reto; }

    public Long getUsuario() { return usuario; }
    public void setUsuario(Long usuario) { this.usuario = usuario; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipacionRetoId that = (ParticipacionRetoId) o;
        return Objects.equals(reto, that.reto) && Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reto, usuario);
    }
}