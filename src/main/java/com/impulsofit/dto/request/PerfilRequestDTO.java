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
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
}
