package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reto")
@NoArgsConstructor
@AllArgsConstructor
public class Reto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reto")
    private Long idReto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo")
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario creador;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    // Meta cuantificable del reto (por ejemplo: 50.0 km, 10 sesiones, etc.)
    @Column(name = "objetivo_total")
    private Double objetivoTotal;

    public Long getIdReto() { return idReto; }
    public void setIdReto(Long idReto) { this.idReto = idReto; }

    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }

    public Usuario getCreador() { return creador; }
    public void setCreador(Usuario creador) { this.creador = creador; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Double getObjetivoTotal() { return objetivoTotal; }
    public void setObjetivoTotal(Double objetivoTotal) { this.objetivoTotal = objetivoTotal; }

    // Métodos de compatibilidad con la versión anterior que usaba ids directamente
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
}
