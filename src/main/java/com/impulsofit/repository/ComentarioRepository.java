package com.impulsofit.repository;

import com.impulsofit.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPublicacion_IdPublicacion(Long publicacionId);

    @Query("""
        SELECT c
        FROM Comentario c
        WHERE c.publicacion.idPublicacion IN :pubIds
        ORDER BY c.fechaCreacion ASC
    """)
    List<Comentario> findByPublicacionIdInOrderByFechaCreacionAsc(@Param("pubIds") List<Long> pubIds);

}
