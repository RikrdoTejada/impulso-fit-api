package com.impulsofit.repository;

import com.impulsofit.model.MembresiaGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembresiaGrupoRepository extends JpaRepository<MembresiaGrupo, Long> {
    boolean existsByPerfil_IdPerfilAndGrupo_IdGrupo(Long perfilIdPerfil, Long grupoIdGrupo);

    // Metodo de compatibilidad para código que usa nombres de propiedades diferentes
    default boolean existsByPerfil_IdAndGrupo_Id(Long idUsuario, Long idGrupo) {
        return existsByPerfil_IdPerfilAndGrupo_IdGrupo(idUsuario, idGrupo);
    }
}