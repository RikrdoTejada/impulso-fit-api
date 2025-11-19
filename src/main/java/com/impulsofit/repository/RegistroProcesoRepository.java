package com.impulsofit.repository;

import com.impulsofit.model.ParticipacionReto;
import com.impulsofit.model.RegistroProceso;
import com.impulsofit.model.Reto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroProcesoRepository extends JpaRepository<RegistroProceso, Long> {
    List<RegistroProceso> findByParticipacionRetoOrderByFechaDesc(ParticipacionReto participacionReto);
    List<RegistroProceso> findByParticipacionReto_Reto(Reto reto);
    List<RegistroProceso> findByParticipacionReto_IdPerfilOrderByFechaDesc(Long idPerfil);
}
