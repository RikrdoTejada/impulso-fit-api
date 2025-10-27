package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo")
    private Long id;

    @Column(name = "id_usuario_creador")
    private Long usuarioCreadorId;

    @Column(name = "id_deporte")
    private Long deporteId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Column(name = "foto_grupo", length = 255)
    private String fotoGrupo;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PublicacionGrupo> publicaciones;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembresiaGrupo> membresias;

    public Grupo() {}

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioCreadorId() { return usuarioCreadorId; }
    public void setUsuarioCreadorId(Long usuarioCreadorId) { this.usuarioCreadorId = usuarioCreadorId; }

    public Long getDeporteId() { return deporteId; }
    public void setDeporteId(Long deporteId) { this.deporteId = deporteId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getFotoGrupo() { return fotoGrupo; }
    public void setFotoGrupo(String fotoGrupo) { this.fotoGrupo = fotoGrupo; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public List<PublicacionGrupo> getPublicaciones() { return publicaciones; }
    public void setPublicaciones(List<PublicacionGrupo> publicaciones) { this.publicaciones = publicaciones; }

    public List<MembresiaGrupo> getMembresias() { return membresias; }
    public void setMembresias(List<MembresiaGrupo> membresias) { this.membresias = membresias; }
}
