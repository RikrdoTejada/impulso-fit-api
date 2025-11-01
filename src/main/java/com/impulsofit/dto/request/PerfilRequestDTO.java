package com.impulsofit.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PerfilRequestDTO {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String nombre;
    private String apellido;
    private String biografia;
    private String ubicacion;
    private String fotoPerfil;

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }

    public String getBiografia() { return biografia; }

    public String getUbicacion() { return ubicacion; }

    public String getFotoPerfil() { return fotoPerfil; }
}
