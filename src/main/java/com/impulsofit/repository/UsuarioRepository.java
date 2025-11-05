package com.impulsofit.repository;

import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar por email (Spring Data ya interpreta el nombre del método)
    Optional<Usuario> findByEmail(String email);

    // Buscar por género (usando JPQL personalizado)
    @Query("SELECT u FROM Usuario u WHERE u.genero = :genero")
    List<Usuario> findByGenero(@Param("genero") String genero);


    boolean existsByEmailIgnoreCase(String email);

    // Obtener todos los usuarios registrados después de cierta fecha
    @Query("SELECT u FROM Usuario u WHERE u.fechaRegistro > :fecha")
    List<Usuario> findUsuariosRegistradosDespues(@Param("fecha") java.time.LocalDate fecha);
}