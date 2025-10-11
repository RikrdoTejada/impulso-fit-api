package com.impulsofit.dto.request;

public class UsuarioDTO {
    private String nombre;
    private String email;
    private String contrasena;
    private Integer edad;
    private String genero;

    public UsuarioDTO() {}

    public UsuarioDTO(String nombre, String email, String contrasena, Integer edad, String genero) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.edad = edad;
        this.genero = genero;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}
