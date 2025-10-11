package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participacion_reto")
@IdClass(ParticipacionRetoId.class)
public class ParticipacionReto {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_reto")
    private Reto reto;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion = LocalDateTime.now();

    // Constructores
    public ParticipacionReto() {}

    public ParticipacionReto(Reto reto, Usuario usuario) {
        this.reto = reto;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Reto getReto() { return reto; }
    public void setReto(Reto reto) { this.reto = reto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaUnion() { return fechaUnion; }
    public void setFechaUnion(LocalDateTime fechaUnion) { this.fechaUnion = fechaUnion; }
}