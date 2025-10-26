package com.impulsofit.repository;

import com.impulsofit.model.PublicacionGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PublicacionGrupoRepository extends JpaRepository<PublicacionGrupo, Long> {

    @Query("SELECT DISTINCT p FROM PublicacionGrupo p " +
            "LEFT JOIN FETCH p.autor " +
            "LEFT JOIN FETCH p.comentarios " +
            "WHERE p.grupo.idGrupo = :grupoId")
    List<PublicacionGrupo> findByGrupoId(@Param("grupoId") Long grupoId);
}
