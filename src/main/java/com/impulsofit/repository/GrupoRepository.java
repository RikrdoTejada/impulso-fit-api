package com.impulsofit.repository;

import com.impulsofit.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Collectors;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    // Buscar por nombre o por deporte (ignore case y permite espacios)
    @Query("SELECT g FROM Grupo g WHERE LOWER(g.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
            "OR LOWER(g.deporte.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Grupo> buscarPorNombreODeporte(@Param("filtro") String filtro);

    // Buscar grupos por id de deporte (consulta explícita para evitar advertencias del analizador)
    @Query("SELECT g FROM Grupo g WHERE g.deporte.idDeporte = :idDeporte")
    List<Grupo> findByDeporte_IdDeporte(@Param("idDeporte") Long idDeporte);

    // Implementación por defecto: reutiliza buscarPorNombreODeporte y filtra por deporteId
    default List<Grupo> buscarPorNombreODeporteYDeporteId(String filtro, Long deporteId) {
        if (deporteId == null) return buscarPorNombreODeporte(filtro);
        return buscarPorNombreODeporte(filtro).stream()
                .filter(g -> g.getDeporte() != null && deporteId.equals(g.getDeporte().getIdDeporte()))
                .collect(Collectors.toList());
    }
}