package com.impulsofit.repository;

import com.impulsofit.model.Publicacion;
import com.impulsofit.model.PublicacionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion,Long> {
    List<Publicacion> findAllByUsuario_IdUsuario(Long usuarioIdUsuario);

    List<Publicacion> findAllByGrupo_IdGrupo(Long grupoIdGrupo);

    Page<Publicacion> findByTypeAndGrupoId(PublicacionType type, Long grupoId, Pageable pageable);

    Page<Publicacion> findByType(PublicacionType type, Pageable pageable);

    List<Publicacion> findAllByTypeAndGrupo_IdGrupo(PublicacionType type, Long grupoId);
}
