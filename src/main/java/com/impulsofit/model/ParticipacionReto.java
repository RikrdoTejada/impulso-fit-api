package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "participacionreto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ParticipacionReto.ParticipacionRetoKey.class)
public class ParticipacionReto {

    @Id
    @Column(name = "id_reto")
    private Long idReto;

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reto", insertable = false, updatable = false)
    private Reto reto;

    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion;

    public static class ParticipacionRetoKey implements Serializable {
        private Long idReto;
        private Long idUsuario;
        // ... equals y hashCode
    }
}