package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "challenges")
@Data @NoArgsConstructor @AllArgsConstructor
public class Challenge {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String titulo;
    private String descripcion;
    private String nivel; // por ejemplo: "facil", "medio", "alto"

    @ManyToOne(optional = false)
    private Group grupo;
}