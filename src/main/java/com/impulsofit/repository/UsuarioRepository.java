package com.impulsofit.repository;
import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    boolean existsByEmailIgnoreCase(String email);
    //boolean existsByEmailIgnoreCaseAndIdUsuarioNot(String email, Long idUsuario);
}
