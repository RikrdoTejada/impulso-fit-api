package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grupo")
@NoArgsConstructor
@AllArgsConstructor
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Long idGrupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_creador", nullable = false)
    private Usuario usuarioCreador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_deporte", nullable = false)
    private Deporte deporte;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT", length = 300)
    private String descripcion;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Column(name = "foto_grupo", length = 255)
    private String fotoGrupo;

    // Guardamos fecha con hora para compatibilidad con usos actuales
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PublicacionGrupo> publicaciones;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembresiaGrupo> membresias;


    // Primary id accessors
    public Long getIdGrupo() { return idGrupo; }
    public void setIdGrupo(Long idGrupo) { this.idGrupo = idGrupo; }

    // Alias para compatibilidad con código que usa getId()/setId()
    public Long getId() { return this.idGrupo; }
    public void setId(Long id) { this.idGrupo = id; }

    // Compatibilidad adicional: entero
    public Integer getIdGrupoAsInteger() { return (this.idGrupo == null) ? null : this.idGrupo.intValue(); }

    // Usuario creador / Deporte
    public Usuario getUsuarioCreador() { return usuarioCreador; }
    public void setUsuarioCreador(Usuario usuarioCreador) { this.usuarioCreador = usuarioCreador; }

    /** Compatibilidad: devuelve el id del usuario creador si la relación está cargada. */
    public Long getUsuarioCreadorId() { return (usuarioCreador != null) ? usuarioCreador.getId() : null; }

    public Deporte getDeporte() { return deporte; }
    public void setDeporte(Deporte deporte) { this.deporte = deporte; }

    /** Compatibilidad: devuelve el id del deporte si la relación está cargada. */
    public Integer getDeporteId() { return (deporte != null) ? deporte.getIdDeporte() : null; }

    // Campos básicos
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getFotoGrupo() { return fotoGrupo; }
    public void setFotoGrupo(String fotoGrupo) { this.fotoGrupo = fotoGrupo; }

    // Fecha creación (varias vistas para compatibilidad)
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDate getFechaCreacionAsLocalDate() { return (this.fechaCreacion == null) ? null : this.fechaCreacion.toLocalDate(); }
    public void setFechaCreacion(LocalDate date) { this.fechaCreacion = (date == null) ? null : date.atStartOfDay(); }

    public Date getFechaCreacionAsDate() { return (this.fechaCreacion == null) ? null : Date.from(this.fechaCreacion.atZone(ZoneId.systemDefault()).toInstant()); }
    public void setFechaCreacionAsDate(Date date) { this.fechaCreacion = (date == null) ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()); }

    // Relaciones listas
    public List<PublicacionGrupo> getPublicaciones() { return publicaciones; }
    public void setPublicaciones(List<PublicacionGrupo> publicaciones) { this.publicaciones = publicaciones; }

    public List<MembresiaGrupo> getMembresias() { return membresias; }
    public void setMembresias(List<MembresiaGrupo> membresias) { this.membresias = membresias; }

    // PrePersist: asegurar fecha por defecto
    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }
}