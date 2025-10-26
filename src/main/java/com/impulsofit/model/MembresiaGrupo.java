package com.impulsofit.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "membresiagrupo",
        uniqueConstraints = @UniqueConstraint(name = "uq_membresia_usuario_grupo",
                columnNames = {"id_usuario","id_grupo"}))
@AllArgsConstructor
@NoArgsConstructor

public class MembresiaGrupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membresia")
    private Long idMembresia;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    @Column(name = "fecha_union", nullable = false)
    private LocalDate fechaUnion;

    @PrePersist
    public void onCreate() {
        this.fechaUnion = LocalDate.now();
    }
}