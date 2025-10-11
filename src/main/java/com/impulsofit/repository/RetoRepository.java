package com.impulsofit.repository;

import com.impulsofit.model.Reto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RetoRepository extends JpaRepository<Reto, Long> {

    // Retos de un grupo específico
    List<Reto> findByGrupoIdGrupo(Long idGrupo);

    // Retos activos (fecha actual entre inicio y fin)
    @Query("SELECT r FROM Reto r WHERE r.fechaInicio <= CURRENT_DATE AND r.fechaFin >= CURRENT_DATE")
    List<Reto> findRetosActivos();
}