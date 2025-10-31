package com.impulsofit.dto.response;

public class PerfilResponseDTO {

    private final Long idPerfil;
    private final String nombre;
    private final String apellido;
    private final String biografia;
    private final String ubicacion;
    private final String fotoPerfil;

    public PerfilResponseDTO(Long idPerfil, String nombre, String apellido, String biografia, String ubicacion, String fotoPerfil) {
        this.idPerfil = idPerfil;
        this.nombre = nombre;
        this.apellido = apellido;
        this.biografia = biografia;
        this.ubicacion = ubicacion;
        this.fotoPerfil = fotoPerfil;
    }

    // Getters
    public Long getIdPerfil() { return idPerfil; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getBiografia() { return biografia; }
    public String getUbicacion() { return ubicacion; }
    public String getFotoPerfil() { return fotoPerfil; }
}
