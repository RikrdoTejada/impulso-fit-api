package com.impulsofit.repository;

import com.impulsofit.model.ParticipacionReto;
import com.impulsofit.model.Perfil;
import com.impulsofit.model.Reto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipacionRetoRepository extends JpaRepository<ParticipacionReto, ParticipacionReto.ParticipacionRetoKey> {
    Optional<ParticipacionReto> findByRetoAndPerfil(Reto reto, Perfil perfil);

    List<ParticipacionReto> findAllByIdPerfil(Long idPerfil);

    long countByIdReto(Long idPerfil);
}