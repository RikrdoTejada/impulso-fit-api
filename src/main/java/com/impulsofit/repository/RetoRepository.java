package com.impulsofit.repository;

import com.impulsofit.model.Reto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RetoRepository extends JpaRepository<Reto, Long> {

    @Query("SELECT r FROM Reto r WHERE LOWER(r.titulo) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Reto> searchByTerm(@Param("term") String term);

    @Query("SELECT r FROM Reto r WHERE r.idGrupo IN (SELECT g.id FROM Grupo g WHERE g.deporte.idDeporte = :deporteId)")
    List<Reto> findByDeporteId(@Param("deporteId") Integer deporteId);

    @Query("SELECT r FROM Reto r WHERE (LOWER(r.titulo) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :term, '%'))) " +
            "AND r.idGrupo IN (SELECT g.id FROM Grupo g WHERE g.deporte.idDeporte = :deporteId)")
    List<Reto> searchByTermAndDeporteId(@Param("term") String term, @Param("deporteId") Integer deporteId);
}
