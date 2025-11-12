package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reaccion")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Reaccion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_reaccion", nullable = false)
    private long idReaccion;

    @ManyToOne
    @JoinColumn(name = "id_publicacion", nullable = false)
    private Publicacion publicacion;

    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil idPerfil;

    @Column(name = "Fecha_Registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
