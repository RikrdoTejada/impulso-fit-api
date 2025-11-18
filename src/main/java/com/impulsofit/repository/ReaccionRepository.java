package com.impulsofit.repository;

import com.impulsofit.model.Perfil;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Reaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReaccionRepository extends JpaRepository<Reaccion,Long> {
    boolean existsReaccionByPerfilAndPublicacion(Perfil perfil, Publicacion publicacion);
    Optional<Reaccion> findByPerfilAndPublicacion(Perfil perfil, Publicacion publicacion);
}
