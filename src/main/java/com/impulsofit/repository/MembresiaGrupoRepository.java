package com.impulsofit.repository;

import com.impulsofit.model.MembresiaGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface MembresiaGrupoRepository extends JpaRepository<MembresiaGrupo, Long> {

    // Verificar si un usuario es miembro de un grupo
    boolean existsByUsuarioIdUsuarioAndGrupoIdGrupo(Long idUsuario, Long idGrupo);

    // Encontrar membresía específica
    Optional<MembresiaGrupo> findByUsuarioIdUsuarioAndGrupoIdGrupo(Long idUsuario, Long idGrupo);

    // Grupos a los que pertenece un usuario
    @Query("SELECT m.grupo.idGrupo FROM MembresiaGrupo m WHERE m.usuario.idUsuario = :idUsuario")
    List<Long> findIdsGruposByUsuario(@Param("idUsuario") Long idUsuario);

    // Contar miembros de un grupo
    Long countByGrupoIdGrupo(Long idGrupo);
}