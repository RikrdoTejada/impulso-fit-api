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

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo_deporte")
    private String tipo;
}