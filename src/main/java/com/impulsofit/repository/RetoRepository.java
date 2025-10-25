package com.impulsofit.repository;
import com.impulsofit.model.Reto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RetoRepository extends JpaRepository<Reto,Long> {
    List<Reto> findAllByGrupo_IdGrupo(Long grupo_id);

    List<Reto> findAllByCreador_IdUsuario(Long creadorIdUsuario);

    boolean existsByNombreIgnoreCaseAndGrupo_IdGrupo(String nombre, Long idGrupo);

    boolean existsByNombreIgnoreCaseAndGrupo_IdGrupoAndIdRetoNot(String nombre, Long idGrupo, Long idReto);
}
