package com.impulsofit.dto.response;

public class LoginResponse {
    private Long idUsuario;
    private String nombre;
    private String email;
    private Integer edad;
    private String genero;

    public LoginResponse(Long idUsuario, String nombre, String email, Integer edad, String genero) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
        this.genero = genero;
    }

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}
