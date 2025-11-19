package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "registroproceso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroProceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Long idRegistro;

    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "id_reto", referencedColumnName = "id_reto", nullable = false),
            @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil", nullable = false)
    })
    private ParticipacionReto participacionReto;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "avance")
    private String avance;
}
