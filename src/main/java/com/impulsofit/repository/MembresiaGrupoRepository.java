package com.impulsofit.repository;

import com.impulsofit.model.MembresiaGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MembresiaGrupoRepository extends JpaRepository<MembresiaGrupo, Long> {

    @Query("SELECT m.idGrupo FROM MembresiaGrupo m WHERE m.idUsuario = :idUsuario")
    List<Long> findIdsGruposByUsuario(Long idUsuario);
}
