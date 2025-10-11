package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "follows",
        uniqueConstraints = @UniqueConstraint(columnNames = {"seguidor_id", "seguido_id"}))
@Data @NoArgsConstructor @AllArgsConstructor
public class Follow {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "seguidor_id")
    private User seguidor;

    @ManyToOne(optional = false) @JoinColumn(name = "seguido_id")
    private User seguido;
}