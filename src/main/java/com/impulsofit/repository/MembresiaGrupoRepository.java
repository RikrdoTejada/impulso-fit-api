package com.impulsofit.repository;

import com.impulsofit.model.MembresiaGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembresiaGrupoRepository extends JpaRepository<MembresiaGrupo, Long> {
    boolean existsByUsuario_IdUsuarioAndGrupo_IdGrupo(Long idUsuario, Long idGrupo);

    // Metodo de compatibilidad para código que usa nombres de propiedades diferentes
    default boolean existsByUsuario_IdAndGrupo_Id(Long idUsuario, Long idGrupo) {
        return existsByUsuario_IdUsuarioAndGrupo_IdGrupo(idUsuario, idGrupo);
    }
}