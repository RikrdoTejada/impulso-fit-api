package com.impulsofit.repository;

import com.impulsofit.model.Comentario;
import com.impulsofit.model.PublicacionGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPublicacionGrupoOrderByFechaCreacionAsc(PublicacionGrupo publicacionGrupo);
}
