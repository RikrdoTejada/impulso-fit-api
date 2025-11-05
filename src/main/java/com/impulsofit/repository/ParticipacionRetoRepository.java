package com.impulsofit.repository;

import com.impulsofit.model.ParticipacionReto;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipacionRetoRepository extends JpaRepository<ParticipacionReto, ParticipacionReto.ParticipacionRetoKey> {
    Optional<ParticipacionReto> findByRetoAndUsuario(Reto reto, Usuario usuario);
}
