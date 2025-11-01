package com.impulsofit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellido_p", nullable = false, length = 100)
    private String apellidoP;

    @Column(name = "apellido_m", nullable = false, length = 100)
    private String apellidoM;

    @Transient
    private String nombre;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "genero", nullable = false)
    private String genero;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado = false;

    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;

    @Column(name = "fecha_bloqueo")
    private LocalDateTime fechaBloqueo;

    @Column(name = "cod_pregunta", nullable = false)
    private CodPregunta codPregunta;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;


    public Long getId() { return idUsuario; }
    public void setId(Long id) { this.idUsuario = id; }

    public String getNombre() {
        if (nombre != null) return nombre;
        StringBuilder sb = new StringBuilder();
        if (nombres != null) sb.append(nombres);
        if (apellidoP != null) sb.append(" ").append(apellidoP);
        if (apellidoM != null) sb.append(" ").append(apellidoM);
        return sb.toString().trim();
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaRegistroAsLocalDate() {
        return this.fechaRegistro != null ? this.fechaRegistro.toLocalDate() : null;
    }

    public void setFechaRegistro(LocalDate fechaRegistroDate) {
        if (fechaRegistroDate == null) {
            this.fechaRegistro = null;
        } else {
            this.fechaRegistro = fechaRegistroDate.atStartOfDay();
        }
    }

    public boolean isBloqueado() {
        return Boolean.TRUE.equals(this.bloqueado);
    }

    // Devuelve la edad calculada a partir de la fecha de nacimiento (si está disponible)
    public Integer getEdad() {
        if (this.fechaNacimiento == null) return null;
        return java.time.Period.between(this.fechaNacimiento, java.time.LocalDate.now()).getYears();
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
        if (this.bloqueado == null) {
            this.bloqueado = false;
        }
        if (this.intentosFallidos == null) {
            this.intentosFallidos = 0;
        }
    }
}