package com.impulsofit.repository;

import com.impulsofit.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByIdPerfil(Long idPerfil);
}
