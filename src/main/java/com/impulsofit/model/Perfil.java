package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "perfil")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Perfil {

    @Id
    @Column(name = "id_perfil")
    private Long idPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(length = 100)
    private String ubicacion;

    @Column(name = "foto_perfil", length = 255)
    private String fotoPerfil;
}