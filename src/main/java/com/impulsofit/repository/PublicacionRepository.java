package com.impulsofit.repository;

import com.impulsofit.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    @Query("""
        SELECT p FROM Publicacion p 
        WHERE p.usuario.idUsuario IN :idsUsuarios 
        OR p.grupo.idGrupo IN :idsGrupos
        ORDER BY p.fechaPublicacion DESC
    """)
    List<Publicacion> findFeedByUsuariosAndGrupos(List<Long> idsUsuarios, List<Long> idsGrupos);

    @Query("SELECT p FROM Publicacion p ORDER BY p.fechaPublicacion DESC")
    List<Publicacion> findAllOrdered();
}
