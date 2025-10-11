package com.impulsofit.repository;

import com.impulsofit.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // 🔹 Feed general de comentarios visibles para el usuario
    @Query("""
        SELECT c FROM Comentario c
        WHERE 
            (c.publicacion.grupo.id IN (
                SELECT m.grupo.id FROM MembresiaGrupo m WHERE m.usuario.id = :userId
            ))
            OR
            (NOT EXISTS (
                SELECT 1 FROM MembresiaGrupo m WHERE m.usuario.id = :userId
            ))
        ORDER BY c.fechaComentario DESC
    """)
    List<Comentario> findFeedByUser(@Param("userId") Long userId);

    // 🔹 Comentarios de una publicación específica (ordenados por fecha ascendente)
    List<Comentario> findByPublicacion_IdPublicacionOrderByFechaComentarioAsc(Long idPublicacion);
}
