package com.impulsofit.repository;

import com.impulsofit.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    // Buscar por nombre o por deporte (ignore case y permite espacios)
    @Query("SELECT g FROM Grupo g WHERE LOWER(g.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
            "OR LOWER(g.deporte.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Grupo> buscarPorNombreODeporte(@Param("filtro") String filtro);
}
