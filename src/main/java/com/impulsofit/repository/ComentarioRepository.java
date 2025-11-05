package com.impulsofit.repository;

import com.impulsofit.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPublicacion_IdPublicacion(Long publicacionId);

    List<Comentario> findByPublicacion_IdPublicacionOrderByFechaCreacionAsc(Long publicacionIdPublicacion);
}
