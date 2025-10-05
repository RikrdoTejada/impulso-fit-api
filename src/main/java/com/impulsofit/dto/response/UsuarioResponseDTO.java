package com.impulsofit.dto.response;

public class UsuarioResponseDTO {
    private Long idUsuario;
    private String nombre;
    private String email;
    private Integer edad;
    private String genero;

    public UsuarioResponseDTO() {}

    public UsuarioResponseDTO(Long idUsuario, String nombre, String email, Integer edad, String genero) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
        this.genero = genero;
    }

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
