package com.impulsofit.model;

import java.io.Serializable;
import java.util.Objects;

public class ListaSeguidoId implements Serializable {
    private Integer idUsuario;  // Cambia a Integer
    private Integer idSeguido;  // Cambia a Integer

    public ListaSeguidoId() {}

    public ListaSeguidoId(Integer idUsuario, Integer idSeguido) {
        this.idUsuario = idUsuario;
        this.idSeguido = idSeguido;
    }

    // Getters y setters con Integer
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdSeguido() { return idSeguido; }
    public void setIdSeguido(Integer idSeguido) { this.idSeguido = idSeguido; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListaSeguidoId that = (ListaSeguidoId) o;
        return Objects.equals(idUsuario, that.idUsuario) &&
                Objects.equals(idSeguido, that.idSeguido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idSeguido);
    }
}