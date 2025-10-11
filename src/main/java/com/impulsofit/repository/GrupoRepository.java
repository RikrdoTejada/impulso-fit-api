package com.impulsofit.repository;

import com.impulsofit.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    // Grupos más populares (ordenados por cantidad de miembros)
    List<Grupo> findTop5ByOrderByCantidadMiembrosDesc();

    // Grupos populares excluyendo los que el usuario ya pertenece
    @Query("SELECT g FROM Grupo g WHERE g.idGrupo NOT IN :gruposUsuario ORDER BY g.cantidadMiembros DESC LIMIT 5")
    List<Grupo> findGruposPopularesNoPertenece(List<Long> gruposUsuario);

    // Todos los grupos ordenados por popularidad
    List<Grupo> findAllByOrderByCantidadMiembrosDesc();
}