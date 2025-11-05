package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "registroproceso")
@Data
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
            @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    })
    private ParticipacionReto participacionReto;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "avance")
    private String avance;
}