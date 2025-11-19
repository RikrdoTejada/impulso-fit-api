package com.impulsofit.service;

import com.impulsofit.model.RegistroProceso;
import com.impulsofit.repository.RegistroProcesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EstadisticaService {

    private final RegistroProcesoRepository registroProcesoRepository;

    @Transactional(readOnly = true)
    public List<LocalDate> diasConProgreso(Long idPerfil) {
        List<RegistroProceso> registros = registroProcesoRepository.findByParticipacionReto_IdPerfilOrderByFechaDesc(idPerfil);
        return registros.stream()
                .map(r -> r.getFecha().toLocalDate())
                .distinct()
                .collect(Collectors.toList());
    }
}
