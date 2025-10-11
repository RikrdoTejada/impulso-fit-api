package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reto")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class Reto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_reto;

    @ManyToOne
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    @ManyToOne
    @JoinColumn(name = "id_usuario_creador", nullable = false)
    private Usuario usuario_creador;

    @ManyToOne
    @JoinColumn(name = "id_unidad", nullable = false)
    private Unidad unidad;

    @Column
    private String nombre;
    @Column
    private String descripcion;
    @Column
    private String objetivo;
    @Column
    private LocalDate fecha_inicio;
    @Column
    private LocalDate fecha_fin;
}
