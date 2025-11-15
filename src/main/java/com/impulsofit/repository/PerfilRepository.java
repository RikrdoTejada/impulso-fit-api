package com.impulsofit.repository;

import com.impulsofit.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    @Query("SELECT p FROM Perfil p WHERE LOWER(CONCAT(COALESCE(p.persona.nombres, ''), ' ', COALESCE(p.persona.apellidoP, ''))) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Perfil> searchByNombreApellido(@Param("term") String term);

    Optional<Perfil> findByIdPerfil(Long idPerfil);

    boolean existsByNombrePerfilIgnoreCase(String nombrePerfil);

    List<Perfil> findAllByPersona_IdPersona(Long idPersona);
}