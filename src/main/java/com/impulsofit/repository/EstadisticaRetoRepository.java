package com.impulsofit.repository;

import com.impulsofit.model.EstadisticaReto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EstadisticaRetoRepository extends JpaRepository<EstadisticaReto, Long> {
    List<EstadisticaReto> findAllByRegistroProceso_ParticipacionReto_IdPerfilAndFechaOrderByFechaDesc(Long idPerfil, LocalDate fecha);
}