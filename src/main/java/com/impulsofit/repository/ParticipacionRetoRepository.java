package com.impulsofit.repository;

import com.impulsofit.model.ParticipacionReto;
import com.impulsofit.model.ParticipacionRetoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface ParticipacionRetoRepository extends JpaRepository<ParticipacionReto, ParticipacionRetoId> {

    // Verificar si usuario participa en reto
    boolean existsByRetoIdRetoAndUsuarioIdUsuario(Long idReto, Long idUsuario);

    // Encontrar participación específica
    Optional<ParticipacionReto> findByRetoIdRetoAndUsuarioIdUsuario(Long idReto, Long idUsuario);

    // Participantes de un reto
    @Query("SELECT p.usuario.idUsuario FROM ParticipacionReto p WHERE p.reto.idReto = :idReto")
    List<Long> findParticipantesByRetoId(@Param("idReto") Long idReto);

    // Retos en los que participa un usuario
    @Query("SELECT p.reto.idReto FROM ParticipacionReto p WHERE p.usuario.idUsuario = :idUsuario")
    List<Long> findRetosParticipandoByUsuario(@Param("idUsuario") Long idUsuario);

    Optional<ParticipacionReto> findByRetoAndUsuario(Reto reto, Usuario usuario);
}