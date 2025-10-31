package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "participacionreto")
@Getter
@Setter
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
        public ParticipacionRetoKey() {}
        public ParticipacionRetoKey(Long idReto, Long idUsuario) { this.idReto = idReto; this.idUsuario = idUsuario; }
        @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof ParticipacionRetoKey k)) return false; return Objects.equals(idReto, k.idReto) && Objects.equals(idUsuario, k.idUsuario); }
        @Override public int hashCode() { return Objects.hash(idReto, idUsuario); }
    }
}
