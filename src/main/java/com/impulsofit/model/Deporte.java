package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deporte")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Deporte
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deporte")
    private Long idDeporte;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "tipo_deporte")
    private String tipo;

    // Compatibilidad: alias getter/setter para código que espera tipoDeporte
    public String getTipoDeporte() { return this.tipo; }
    public void setTipoDeporte(String tipoDeporte) { this.tipo = tipoDeporte; }

    // Compatibilidad: devolver id como Integer si se usa en DTOs antiguos
    public Integer getIdDeporteAsInteger() { return this.idDeporte == null ? null : this.idDeporte.intValue(); }
}
