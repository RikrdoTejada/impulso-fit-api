package com.impulsofit.repository;

import com.impulsofit.model.EstadisticaReto;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadisticaRetoRepository extends JpaRepository<EstadisticaReto, Long> {
    Optional<EstadisticaReto> findByRetoAndUsuario(Reto reto, Usuario usuario);
}
