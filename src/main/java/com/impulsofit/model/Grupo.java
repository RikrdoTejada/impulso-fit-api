package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "grupo")
@Getter
@Setter
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Long idGrupo;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
    private String ubicacion;

    @Column(name = "id_deporte")
    private Long idDeporte;

    @ManyToOne
    @JoinColumn(name = "id_usuario_creador")
    private Usuario usuarioCreador;
}
