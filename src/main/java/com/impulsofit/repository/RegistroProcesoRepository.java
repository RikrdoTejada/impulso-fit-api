package com.impulsofit.repository;

import com.impulsofit.model.RegistroProceso;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
public interface RegistroProcesoRepository extends JpaRepository<RegistroProgreso, Long> {
=======
public interface RegistroProcesoRepository extends JpaRepository<RegistroProceso, Long> {
>>>>>>> e35f24085b89cc1b6ce7868fbffef09f4d0ff951

    // Encontrar registro específico
    Optional<RegistroProceso> findByRetoAndUsuarioAndFecha(Reto reto, Usuario usuario, LocalDate fecha);

    // Contar días completados por usuario en reto
    @Query("SELECT COUNT(r) FROM RegistroProceso r WHERE r.reto.idReto = :idReto AND r.usuario.idUsuario = :idUsuario AND r.completado = true")
    Long countByRetoIdRetoAndUsuarioIdUsuarioAndCompletadoTrue(@Param("idReto") Long idReto, @Param("idUsuario") Long idUsuario);

    // Sumar puntos por usuario en reto
    @Query("SELECT COALESCE(SUM(r.puntos), 0) FROM RegistroProceso r WHERE r.reto.idReto = :idReto AND r.usuario.idUsuario = :idUsuario")
    Integer sumPuntosByRetoAndUsuario(@Param("idReto") Long idReto, @Param("idUsuario") Long idUsuario);

    // Verificar si existen registros para un reto
    boolean existsByRetoIdReto(Long idReto);

    // Obtener todos los registros de un reto
    List<RegistroProceso> findByRetoIdReto(Long idReto);

    // Obtener progreso de un usuario en un reto
    List<RegistroProceso> findByRetoIdRetoAndUsuarioIdUsuario(Long idReto, Long idUsuario);

    // Obtener registros por fecha específica
    List<RegistroProceso> findByRetoIdRetoAndFecha(Long idReto, LocalDate fecha);
}