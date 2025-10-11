package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrupo;

    @Column(name = "id_usuario_creador", nullable = false)
    private Long idUsuarioCreador;

    @Column(name = "id_deporte", nullable = false)
    private Long idDeporte;

    private String nombre;
    private String descripcion;
    private String ubicacion;
    private String fotoGrupo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion = LocalDate.now();

    private Integer cantidadMiembros = 0; // Campo adicional para popularidad

    // Constructores
    public Grupo() {}

    public Grupo(Long idUsuarioCreador, Long idDeporte, String nombre, String descripcion,
                 String ubicacion, String fotoGrupo, Integer cantidadMiembros) {
        this.idUsuarioCreador = idUsuarioCreador;
        this.idDeporte = idDeporte;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fotoGrupo = fotoGrupo;
        this.cantidadMiembros = cantidadMiembros;
        this.fechaCreacion = LocalDate.now();
    }

    // Getters y Setters
    public Long getIdGrupo() { return idGrupo; }
    public void setIdGrupo(Long idGrupo) { this.idGrupo = idGrupo; }

    public Long getIdUsuarioCreador() { return idUsuarioCreador; }
    public void setIdUsuarioCreador(Long idUsuarioCreador) { this.idUsuarioCreador = idUsuarioCreador; }

    public Long getIdDeporte() { return idDeporte; }
    public void setIdDeporte(Long idDeporte) { this.idDeporte = idDeporte; }

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

    public Integer getCantidadMiembros() { return cantidadMiembros; }
    public void setCantidadMiembros(Integer cantidadMiembros) { this.cantidadMiembros = cantidadMiembros; }
}