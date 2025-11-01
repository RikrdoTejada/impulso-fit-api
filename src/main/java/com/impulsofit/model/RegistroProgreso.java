package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registro_progreso")
public class RegistroProgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRegistro;

    @ManyToOne
    @JoinColumn(name = "id_reto", nullable = false)
    private Reto reto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "completado", nullable = false)
    private Boolean completado = false;

    @Column(name = "puntos")
    private Integer puntos = 0;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro = LocalDate.now();

    // Constructores
    public RegistroProgreso() {}

    public RegistroProgreso(Reto reto, Usuario usuario, LocalDate fecha) {
        this.reto = reto;
        this.usuario = usuario;
        this.fecha = fecha;
    }

    public RegistroProgreso(Reto reto, Usuario usuario, LocalDate fecha, Boolean completado, Integer puntos) {
        this.reto = reto;
        this.usuario = usuario;
        this.fecha = fecha;
        this.completado = completado;
        this.puntos = puntos;
    }

    // Getters y Setters
    public Long getIdRegistro() { return idRegistro; }
    public void setIdRegistro(Long idRegistro) { this.idRegistro = idRegistro; }

    public Reto getReto() { return reto; }
    public void setReto(Reto reto) { this.reto = reto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Boolean getCompletado() { return completado; }
    public void setCompletado(Boolean completado) { this.completado = completado; }

    public Integer getPuntos() { return puntos; }
    public void setPuntos(Integer puntos) { this.puntos = puntos; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}