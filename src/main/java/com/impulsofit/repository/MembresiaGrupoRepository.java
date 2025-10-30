package com.impulsofit.repository;

import com.impulsofit.model.MembresiaGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembresiaGrupoRepository extends JpaRepository<MembresiaGrupo, Long> {
    boolean existsByUsuario_IdAndGrupo_Id(Long usuarioId, Long grupoId);
}
