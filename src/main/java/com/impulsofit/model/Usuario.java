package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
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

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellido_p", nullable = false)
    private String apellidoP;

    @Column(name = "apellido_m", nullable = false)
    private String apellidoM;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "genero", nullable = false)
    private String genero;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "cod_pregunta", nullable = false)
    private CodPregunta codPregunta;

    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado = false;

    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;

    @Column(name = "fecha_bloqueo")
    private LocalDateTime fechaBloqueo;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;

    public Long getId() { return idUsuario; }
    public void setId(Long id) { this.idUsuario = id; }

    public String getNombres() {
        StringBuilder sb = new StringBuilder();
        if (nombres != null) sb.append(nombres);
        if (apellidoP != null) sb.append(" ").append(apellidoP);
        if (apellidoM != null) sb.append(" ").append(apellidoM);
        return sb.toString().trim();
    }

    public LocalDate getFechaRegistroAsLocalDate() {
        return this.fechaRegistro != null ? this.fechaRegistro.toLocalDate() : null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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

    // Devuelve la edad calculada a partir de la fecha de nacimiento
    public Integer getEdad() {
        if (this.fechaNacimiento == null) return null;
        return java.time.Period.between(this.fechaNacimiento, java.time.LocalDate.now()).getYears();
    }

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}