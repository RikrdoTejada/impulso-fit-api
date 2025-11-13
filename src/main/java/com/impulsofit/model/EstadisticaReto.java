// ...existing code...
package com.impulsofit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "estadisticareto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticaReto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estadistica")
    private Long idEstadistica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_registro")
    private RegistroProceso registroProceso;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Transient
    public Long getIdReto() {
        if (registroProceso != null && registroProceso.getParticipacionReto() != null) {
            return registroProceso.getParticipacionReto().getIdReto();
        }
        return null;
    }

    @Transient
    public Long getIdPerfil() {
        if (registroProceso != null && registroProceso.getParticipacionReto() != null) {
            return registroProceso.getParticipacionReto().getIdPerfil();
        }
        return null;
    }
}
