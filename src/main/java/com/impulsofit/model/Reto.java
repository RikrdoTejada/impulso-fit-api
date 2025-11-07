package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "challenges")
public class Reto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
    private Usuario createdBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    // ====== getters & setters ======
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public Instant getCreatedAt() { return createdAt; }
    public Usuario getCreatedBy() { return createdBy; }
    public Grupo getGroup() { return grupo; }

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
        return (this.creador != null) ? this.creador.getIdUsuario() : null;
    }

    public void setIdUsuario(Long idUsuario) {
        if (idUsuario == null) { this.creador = null; return; }
        if (this.creador == null) this.creador = new Usuario();
        this.creador.setIdUsuario(idUsuario);
    }

    @PrePersist
    public void onCreate() {
        if (this.fechaPublicacion == null) {
            this.fechaPublicacion = LocalDate.now();
        }
    }
}