package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "seguidos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seguido {

    @EmbeddedId
    private SeguidoId id;

    @Column(name = "fecha_seguido")
    private LocalDateTime fechaSeguido;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SeguidoId implements Serializable {
        @Column(name = "id_seguido")
        private Long idSeguido;

        @Column(name = "id_seguidor")
        private Long idSeguidor;
    }
}
