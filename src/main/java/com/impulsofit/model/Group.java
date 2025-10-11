package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "groups")
@Data @NoArgsConstructor @AllArgsConstructor
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String nombre;
    private String descripcion;

    // opcional: creador del grupo
    @ManyToOne
    private User creador;
}
