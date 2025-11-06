package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "listaseguido")
@IdClass(ListaSeguidoId.class)
public class ListaSeguido {
    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;  // Cambia a Integer

    @Id
    @Column(name = "id_seguido")
    private Integer idSeguido;  // Cambia a Integer

    private LocalDateTime fecha;

}