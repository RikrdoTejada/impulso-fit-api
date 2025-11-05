package com.impulsofit.repository;

import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    // Por entidad usuario
    List<Publicacion> findAllByUser(Usuario usuario);

    // Por id de usuario (lo que usa el servicio)
    List<Publicacion> findAllByUserId(Long userId);

    // Útiles para otros listados
    List<Publicacion> findAllByGroupId(Long groupId);
    List<Publicacion> findAllByChallengeId(Long challengeId);
}