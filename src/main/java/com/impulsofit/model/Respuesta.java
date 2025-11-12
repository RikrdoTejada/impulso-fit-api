package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "ansrecover")
@AllArgsConstructor
@NoArgsConstructor

public class Respuesta{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_respuesta", nullable = false)
    private Long idRespuesta;

    @OneToOne
    @JoinColumn(name="id_perfil", nullable = false)
    private Perfil perfil;

    @Column(name = "str_respuesta", nullable = false)
    private String strRespuesta;

}
