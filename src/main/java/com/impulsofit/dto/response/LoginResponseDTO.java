package com.impulsofit.dto.response;

public class LoginResponseDTO {
    private final Long idUsuario;
    private final String nombre;
    private final String email;
    private final Integer edad;
    private final String genero;

    public LoginResponseDTO(Long idUsuario, String nombre, String email, Integer edad, String genero) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.edad = edad;
        this.genero = genero;
    }

    // Getters
    public Long getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Integer getEdad() { return edad; }
    public String getGenero() { return genero; }
}
