package com.impulsofit.model;

import jakarta.persistence.*;

/**
 * Entidad Perfil: comparte PK con Usuario (1:1) usando @MapsId.
 * Esto permite que el perfil tenga exactamente el mismo id que su usuario asociado.
 */
@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @Column(name = "id_perfil")
    private Long idPerfil; // será el mismo valor que Usuario.id (no generado aquí)

    @Column(length = 50)
    private String nombre;

    @Column(length = 50)
    private String apellido;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(length = 100)
    private String ubicacion;

    @Column(name = "foto_perfil", length = 255)
    private String fotoPerfil;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_perfil")
    private Usuario usuario;

    public Perfil() {}

    // Getters / Setters
    public Long getIdPerfil() { return idPerfil; }
    public void setIdPerfil(Long idPerfil) { this.idPerfil = idPerfil; }

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

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.idPerfil = (usuario != null) ? usuario.getId() : null;
    }
}