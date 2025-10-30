package com.impulsofit.repository;

import com.impulsofit.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion,Long> {
    List<Publicacion> findAllByUsuario_IdUsuario(Long usuarioIdUsuario);

    List<Publicacion> findAllByGrupo_IdGrupo(Long grupoIdGrupo);
}
