package com.impulsofit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comentario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Long id;

    @Column(name = "contenido", nullable = false, length = 500)
    private String contenido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private Publicacion publicacion;

    @Column(name = "fecha_comentario", nullable = false)
    private LocalDateTime fechaCreacion;

    // Tipo genérico para distinguir origen de la publicación (ej. 'GENERAL' o 'GRUPAL')
    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo = "GENERAL";

    // Constructor con campos
    public Comentario(String contenido, Usuario usuario, Publicacion publicacion) {
        this.contenido = contenido;
        this.usuario = usuario;
        this.publicacion = publicacion;
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.tipo == null) {
            this.tipo = "GENERAL";
        }
    }
}