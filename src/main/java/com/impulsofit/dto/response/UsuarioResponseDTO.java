package com.impulsofit.dto.response;

public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String perfilUrl;

    public UsuarioResponseDTO() {}
    public UsuarioResponseDTO(Long id, String nombre, String perfilUrl) {
        this.id = id;
        this.nombre = nombre;
        this.perfilUrl = perfilUrl;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getPerfilUrl() { return perfilUrl; }
}

