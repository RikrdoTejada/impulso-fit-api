package com.impulsofit.repository;

import com.impulsofit.model.Comentario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    @Query("""
        SELECT c FROM Comentario c
        WHERE 
            (c.grupo.id IN (
                SELECT m.grupo.id FROM MembresiaGrupo m WHERE m.usuario.id = :userId
            ))
            OR
            (NOT EXISTS (
                SELECT 1 FROM MembresiaGrupo m WHERE m.usuario.id = :userId
            ))
        ORDER BY c.fechaCreacion DESC
    """)
    List<Comentario> findFeedByUser(@Param("userId") Long userId);
}
