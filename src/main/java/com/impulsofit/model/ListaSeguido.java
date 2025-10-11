package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listaseguido")
@IdClass(ListaSeguidoId.class)
public class ListaSeguido {
    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;  // Cambia a Integer

    @Id
    @Column(name = "id_seguido")
    private Integer idSeguido;  // Cambia a Integer

    private LocalDateTime fecha;

    // Getters y setters con Integer
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdSeguido() { return idSeguido; }
    public void setIdSeguido(Integer idSeguido) { this.idSeguido = idSeguido; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}