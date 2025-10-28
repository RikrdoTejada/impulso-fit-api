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

public class Respuesta {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_respuesta", nullable = false)
    private Long idRespuesta;

    @OneToOne
    @JoinColumn(name="id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "str_respueta", nullable = false)
    private String strRespuesta;

}
