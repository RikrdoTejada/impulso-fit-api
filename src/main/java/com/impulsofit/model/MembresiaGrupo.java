package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "membresiagrupo",
        uniqueConstraints = @UniqueConstraint(name = "uq_membresia_usuario_grupo",
                columnNames = {"id_usuario", "id_grupo"}))
@AllArgsConstructor
@NoArgsConstructor
public class MembresiaGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membresia")
    private Long idMembresia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil idPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    @Column(name = "fecha_union", nullable = false)
    private LocalDateTime fechaUnion;

    public Long getId() { return this.idMembresia; }
    public void setId(Long id) { this.idMembresia = id; }

    @PrePersist
    public void onCreate() {
        if (this.fechaUnion == null) {
            this.fechaUnion = LocalDateTime.now();
        }
    }
}