package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "grupo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Long idGrupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_creador", nullable = false)
    private Usuario creador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_deporte", nullable = false)
    private Deporte deporte;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT", length = 300)
    private String descripcion;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "foto_grupo")
    private String fotoGrupo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public Long getId() { return this.idGrupo; }
    public void setId(Long id) { this.idGrupo = id; }

    public Integer getIdGrupoAsInteger() { return (this.idGrupo == null) ? null : this.idGrupo.intValue(); }

    public Usuario getCreador() { return this.creador; }
    public void setCreador(Usuario creador) { this.creador = creador; }

    public Long getUsuarioCreadorId() { return (this.creador != null) ? this.creador.getIdUsuario() : null; }

    public Long getDeporteId() { return (this.deporte != null) ? this.deporte.getIdDeporte() : null; }

    public LocalDate getFechaCreacionAsLocalDate() { return (this.fechaCreacion == null) ? null : this.fechaCreacion.toLocalDate(); }
    public void setFechaCreacion(LocalDate date) { this.fechaCreacion = (date == null) ? null : date.atStartOfDay(); }

    public Date getFechaCreacionAsDate() { return (this.fechaCreacion == null) ? null : Date.from(this.fechaCreacion.atZone(ZoneId.systemDefault()).toInstant()); }
    public void setFechaCreacionAsDate(Date date) { this.fechaCreacion = (date == null) ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()); }

    // PrePersist: asegurar fecha por defecto
    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }
}
