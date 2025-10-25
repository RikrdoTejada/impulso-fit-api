package com.impulsofit.repository;
import com.impulsofit.model.MembresiaGrupo;
import org.springframework.data.repository.CrudRepository;

public interface MembresiaGrupoRepository
extends CrudRepository<MembresiaGrupo, Long>
{
    boolean existsByUsuario_IdUsuarioAndGrupo_IdGrupo(Long idUsuario, Long idGrupo);
}


