package com.impulsofit.repository;

import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Usuario> findByEmail(String email);
}