package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estadistica_reto")
public class EstadisticaReto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstadistica;

    @ManyToOne
    @JoinColumn(name = "id_reto", nullable = false)
    private Reto reto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "dias_completados")
    private Integer diasCompletados = 0;

    @Column(name = "total_dias")
    private Integer totalDias = 0;

    @Column(name = "porcentaje_cumplimiento")
    private Double porcentajeCumplimiento = 0.0;

    @Column(name = "puntos_totales")
    private Integer puntosTotales = 0;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    // Constructores
    public EstadisticaReto() {}

    public EstadisticaReto(Reto reto, Usuario usuario) {
        this.reto = reto;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getIdEstadistica() { return idEstadistica; }
    public void setIdEstadistica(Long idEstadistica) { this.idEstadistica = idEstadistica; }

    public Reto getReto() { return reto; }
    public void setReto(Reto reto) { this.reto = reto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Integer getDiasCompletados() { return diasCompletados; }
    public void setDiasCompletados(Integer diasCompletados) { this.diasCompletados = diasCompletados; }

    public Integer getTotalDias() { return totalDias; }
    public void setTotalDias(Integer totalDias) { this.totalDias = totalDias; }

    public Double getPorcentajeCumplimiento() { return porcentajeCumplimiento; }
    public void setPorcentajeCumplimiento(Double porcentajeCumplimiento) { this.porcentajeCumplimiento = porcentajeCumplimiento; }

    public Integer getPuntosTotales() { return puntosTotales; }
    public void setPuntosTotales(Integer puntosTotales) { this.puntosTotales = puntosTotales; }

    public Integer getRanking() { return ranking; }
    public void setRanking(Integer ranking) { this.ranking = ranking; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}