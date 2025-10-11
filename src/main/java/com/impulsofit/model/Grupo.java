package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "grupo")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_grupo;

    @ManyToOne
    @JoinColumn(name = "id_usuario_creador", nullable = false)
    private Usuario usuario_creador;

    @ManyToOne
    @JoinColumn(name = "id_deporte", nullable = false)
    private Deporte deporte;

    @Column
    private String nombre;
    @Column
    private String descripcion;
    @Column
    private String ubicacion;
    @Column
    private LocalDate fecha_creacion;

}