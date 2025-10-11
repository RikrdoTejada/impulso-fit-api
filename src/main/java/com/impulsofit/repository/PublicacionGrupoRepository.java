package com.impulsofit.repository;

import com.impulsofit.model.PublicacionGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PublicacionGrupoRepository extends JpaRepository<PublicacionGrupo, Long> {
    List<PublicacionGrupo> findByGrupoIdOrderByFechaCreacionDesc(Long grupoId);
}
