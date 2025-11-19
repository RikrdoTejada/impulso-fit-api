package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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
    @Column(name = "id_perfil")
    private Long idPerfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", insertable = false, updatable = false)
    private Perfil perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reto", insertable = false, updatable = false)
    private Reto reto;

    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion;

    public static class ParticipacionRetoKey implements Serializable {
        private Long idReto;
        private Long idPerfil;
        public ParticipacionRetoKey() {}
        public ParticipacionRetoKey(Long idReto, Long idPerfil) { this.idReto = idReto; this.idPerfil = idPerfil; }
        @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof ParticipacionRetoKey k)) return false; return Objects.equals(idReto, k.idReto) && Objects.equals(idPerfil, k.idPerfil); }
        @Override public int hashCode() { return Objects.hash(idReto, idPerfil ); }
    }
}
