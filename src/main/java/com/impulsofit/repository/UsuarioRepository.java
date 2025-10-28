package com.impulsofit.repository;

import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByNombreIgnoreCaseContaining(String nombre);
}

