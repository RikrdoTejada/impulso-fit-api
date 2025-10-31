package com.impulsofit.repository;

import com.impulsofit.model.Reto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RetoRepository extends JpaRepository<Reto, Long> {

    @Query("SELECT r FROM Reto r WHERE " +
            "LOWER(COALESCE(r.titulo, r.nombre)) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Reto> searchByTerm(@Param("term") String term);

    @Query("SELECT r FROM Reto r WHERE r.grupo.deporte.idDeporte = :deporteId")
    List<Reto> findByDeporteId(@Param("deporteId") Long deporteId);

    @Query("SELECT r FROM Reto r WHERE " +
            "(LOWER(COALESCE(r.titulo, r.nombre)) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :term, '%'))) " +
            "AND r.grupo.deporte.idDeporte = :deporteId")
    List<Reto> searchByTermAndDeporteId(@Param("term") String term, @Param("deporteId") Long deporteId);

    List<Reto> findAllByGrupo_IdGrupo(Long idGrupo);

    List<Reto> findAllByCreador_IdUsuario(Long idUsuario);

    List<Reto> findAllByUnidad_IdUnidad(Long idUnidad);

    boolean existsByNombreIgnoreCaseAndGrupo_IdGrupo(String nombre, Long idGrupo);
    boolean existsByNombreIgnoreCaseAndGrupo_IdGrupoAndIdRetoNot(String nombre, Long idGrupo, Long idReto);

    boolean existsByTituloIgnoreCaseAndGrupo_IdGrupo(String titulo, Long idGrupo);
    boolean existsByTituloIgnoreCaseAndGrupo_IdGrupoAndIdRetoNot(String titulo, Long idGrupo, Long idReto);
}