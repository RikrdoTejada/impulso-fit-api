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
    @Column(name = "id_reto")
    private Long idReto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_creador", nullable = false)
    private Usuario creador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad")
    private Unidad unidad;

    @Column(name = "titulo", length = 255)
    private String titulo;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // Meta cuantificable
    @Column(name = "objetivo_total")
    private Double objetivoTotal;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    public Long getIdGrupo() {
        return (this.grupo != null) ? this.grupo.getId() : null;
    }

    public void setIdGrupo(Long idGrupo) {
        if (idGrupo == null) { this.grupo = null; return; }
        if (this.grupo == null) this.grupo = new Grupo();
        this.grupo.setId(idGrupo);
    }

    public Long getIdUsuario() {
        return (this.creador != null) ? this.creador.getId() : null;
    }

    public void setIdUsuario(Long idUsuario) {
        if (idUsuario == null) { this.creador = null; return; }
        if (this.creador == null) this.creador = new Usuario();
        this.creador.setId(idUsuario);
    }

    @PrePersist
    public void onCreate() {
        if (this.fechaPublicacion == null) {
            this.fechaPublicacion = LocalDate.now();
        }
    }
}