package com.impulsofit.dto.response;

public class PerfilResponse {

    private Long idPerfil;
    private String nombre;
    private String apellido;
    private String biografia;
    private String ubicacion;
    private String fotoPerfil;

    // Getters
    public Long getIdPerfil() { return idPerfil; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getBiografia() { return biografia; }
    public String getUbicacion() { return ubicacion; }
    public String getFotoPerfil() { return fotoPerfil; }

    // Setters
    public void setIdPerfil(Long idPerfil) { this.idPerfil = idPerfil; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
}
