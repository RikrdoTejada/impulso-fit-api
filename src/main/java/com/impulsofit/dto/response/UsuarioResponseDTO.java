package com.impulsofit.dto.response;

import com.impulsofit.model.CodPregunta;

import java.time.LocalDate;

public class UsuarioResponseDTO {
    private Long id_usuario;
    private String nombres;
    private String apellido_p;
    private String apellido_m;
    private String email;
    private String contrasena;
    private LocalDate fecha_Nacimiento;
    private String genero;
    private LocalDate fecha_creacion;
    private CodPregunta cod_pregunta;

    public UsuarioResponseDTO() {}

    // Constructor compacto (id, nombre visible, perfilUrl) conservado por compatibilidad anterior
    private String perfilUrl; // legacy field used elsewhere
    public UsuarioResponseDTO(Long id, String nombre, String perfilUrl) {
        this.id_usuario = id;
        this.nombres = nombre;
        this.perfilUrl = perfilUrl;
    }

    // Constructor completo (mismo orden que el record original)
    public UsuarioResponseDTO(Long id_usuario, String nombres, String apellido_p, String apellido_m, String email, String contrasena, LocalDate fecha_Nacimiento, String genero, LocalDate fecha_creacion, CodPregunta cod_pregunta) {
        this.id_usuario = id_usuario;
        this.nombres = nombres;
        this.apellido_p = apellido_p;
        this.apellido_m = apellido_m;
        this.email = email;
        this.contrasena = contrasena;
        this.fecha_Nacimiento = fecha_Nacimiento;
        this.genero = genero;
        this.fecha_creacion = fecha_creacion;
        this.cod_pregunta = cod_pregunta;
    }

    // Getters y setters
    public Long getId_usuario() { return id_usuario; }
    public void setId_usuario(Long id_usuario) { this.id_usuario = id_usuario; }

    // Compatibilidad con getId()
    public Long getId() { return id_usuario; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellido_p() { return apellido_p; }
    public void setApellido_p(String apellido_p) { this.apellido_p = apellido_p; }

    public String getApellido_m() { return apellido_m; }
    public void setApellido_m(String apellido_m) { this.apellido_m = apellido_m; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public LocalDate getFecha_Nacimiento() { return fecha_Nacimiento; }
    public void setFecha_Nacimiento(LocalDate fecha_Nacimiento) { this.fecha_Nacimiento = fecha_Nacimiento; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public LocalDate getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(LocalDate fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    public CodPregunta getCod_pregunta() { return cod_pregunta; }
    public void setCod_pregunta(CodPregunta cod_pregunta) { this.cod_pregunta = cod_pregunta; }

    // legacy perfilUrl
    public String getPerfilUrl() { return perfilUrl; }
    public void setPerfilUrl(String perfilUrl) { this.perfilUrl = perfilUrl; }
}
