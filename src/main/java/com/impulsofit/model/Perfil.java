package com.impulsofit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Long idPerfil;

    @Column(length = 50)
    private String nombre;

    @Column(length = 50)
    private String apellido;

    @Column(columnDefinition = "text")
    private String biografia;

    @Column(length = 100)
    private String ubicacion;

    @Column(name = "foto_perfil", length = 255)
    private String fotoPerfil;

    // ELIMINAMOS o COMENTAMOS la relación con Usuario para que no cause problemas
    // @OneToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    // private Usuario usuario;

    public Perfil() {}

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

    // Getters y Setters de usuario ELIMINADOS
}
