package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "participacion_reto")
@Getter
@Setter
public class ParticipacionReto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParticipacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_reto")
    private Reto reto;

    private LocalDateTime fechaUnion;
}
