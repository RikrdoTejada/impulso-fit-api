package com.impulsofit.repository;

import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Reaccion;
import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaccionRepository extends JpaRepository<Reaccion,Long> {
    boolean existsReaccionByUsuarioAndPublicacion(Usuario usuario, Publicacion publicacion);
}
