package com.impulsofit.repository;

import com.impulsofit.model.Publicacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    // por id de usuario (no rompe el LAZY)
    List<Publicacion> findAllByUsuarioId(Long usuarioId);

    // opcional: por entidad completa
    // List<Publicacion> findAllByUsuario(Usuario usuario);
}