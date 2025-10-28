package com.impulsofit.repository;

import com.impulsofit.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Integer> {

    // Buscar por nombre o por deporte (ignore case y permite espacios)
    @Query("SELECT g FROM Grupo g WHERE LOWER(g.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
            "OR LOWER(g.deporte.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Grupo> buscarPorNombreODeporte(@Param("filtro") String filtro);

    // Buscar grupos por id de deporte
    List<Grupo> findByDeporte_IdDeporte(Integer idDeporte);

    // Buscar por filtro y por id de deporte
    @Query("SELECT g FROM Grupo g WHERE (LOWER(g.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
            "OR LOWER(g.deporte.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))) AND g.deporte.idDeporte = :deporteId")
    List<Grupo> buscarPorNombreODeporteYDeporteId(@Param("filtro") String filtro, @Param("deporteId") Integer deporteId);
}
