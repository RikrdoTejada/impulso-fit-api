package com.impulsofit.repository;

import com.impulsofit.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> { // Cambiar a Long

    @Query("""
        SELECT p FROM Publicacion p 
        WHERE p.usuario.idUsuario IN :idsUsuarios 
        OR (p.grupo IS NOT NULL AND p.grupo.idGrupo IN :idsGrupos)
        ORDER BY p.fechaPublicacion DESC
    """)
    List<Publicacion> findFeedByUsuariosAndGrupos(@Param("idsUsuarios") List<Long> idsUsuarios, // Cambiar a Long
                                                  @Param("idsGrupos") List<Long> idsGrupos);

    @Query("SELECT p FROM Publicacion p ORDER BY p.fechaPublicacion DESC")
    List<Publicacion> findAllOrdered();
}