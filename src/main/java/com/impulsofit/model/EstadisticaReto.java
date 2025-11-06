package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "estadistica_reto")
public class EstadisticaReto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estadistica", nullable = false)
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

}