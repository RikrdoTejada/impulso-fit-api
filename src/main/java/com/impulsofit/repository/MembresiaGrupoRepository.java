package com.impulsofit.repository;

import com.impulsofit.model.MembresiaGrupo;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembresiaGrupoRepository extends JpaRepository<MembresiaGrupo, Long> {
    boolean existsByGrupoAndUsuario(Grupo grupo, Usuario usuario);
}
