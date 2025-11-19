package com.impulsofit.repository;

import com.impulsofit.model.Persona;
import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByUsuario(Usuario usuario);
}
