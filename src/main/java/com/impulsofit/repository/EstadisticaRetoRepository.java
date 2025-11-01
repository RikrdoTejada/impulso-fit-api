package com.impulsofit.repository;

import com.impulsofit.model.EstadisticaReto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EstadisticaRetoRepository extends JpaRepository<EstadisticaReto, Long> {

    // Buscar estadística específica de usuario en reto
    Optional<EstadisticaReto> findByRetoIdRetoAndUsuarioIdUsuario(Long idReto, Long idUsuario);

    // Obtener todas las estadísticas de un reto ordenadas por ranking
    List<EstadisticaReto> findByRetoIdRetoOrderByRankingAsc(Long idReto);

    // Obtener ranking de un usuario específico
    @Query("SELECT e.ranking FROM EstadisticaReto e WHERE e.reto.idReto = :idReto AND e.usuario.idUsuario = :idUsuario")
    Optional<Integer> findRankingByRetoAndUsuario(@Param("idReto") Long idReto, @Param("idUsuario") Long idUsuario);

    // Contar participantes en un reto
    Long countByRetoIdReto(Long idReto);
}