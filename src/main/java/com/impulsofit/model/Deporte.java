package com.impulsofit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "deporte")
public class Deporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deporte")
    private Long idDeporte;

    private String nombre;
    private String tipoDeporte;

    public Long getIdDeporte() { return idDeporte; }
    public void setIdDeporte(Long idDeporte) { this.idDeporte = idDeporte; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoDeporte() { return tipoDeporte; }
    public void setTipoDeporte(String tipoDeporte) { this.tipoDeporte = tipoDeporte; }
}
