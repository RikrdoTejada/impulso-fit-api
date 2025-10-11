package com.impulsofit.service;

import com.impulsofit.model.ParticipacionReto;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.ParticipacionRetoRepository;
import com.impulsofit.repository.EstadisticaRetoRepository;
import com.impulsofit.model.EstadisticaReto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ParticipacionRetoService {

    @Autowired
    private ParticipacionRetoRepository participacionRetoRepository;

    @Autowired
    private EstadisticaRetoRepository estadisticaRetoRepository;

    public ParticipacionReto unirseAReto(Usuario usuario, Reto reto) {
        Optional<ParticipacionReto> existing = participacionRetoRepository.findByRetoAndUsuario(reto, usuario);
        if (existing.isPresent()) {
            return existing.get();
        }
        ParticipacionReto participacion = new ParticipacionReto();
        participacion.setUsuario(usuario);
        participacion.setReto(reto);
        participacion.setFechaUnion(LocalDateTime.now());
        participacionRetoRepository.save(participacion);

        EstadisticaReto estadistica = new EstadisticaReto();
        estadistica.setUsuario(usuario);
        estadistica.setReto(reto);
        estadistica.setProgreso(0.0);
        estadistica.setFechaActualizacion(LocalDateTime.now());
        estadisticaRetoRepository.save(estadistica);

        return participacion;
    }

    public EstadisticaReto agregarProgreso(Usuario usuario, Reto reto, Double progreso) {
        Optional<EstadisticaReto> optEstadistica = estadisticaRetoRepository.findByRetoAndUsuario(reto, usuario);
        if (optEstadistica.isPresent()) {
            EstadisticaReto estadistica = optEstadistica.get();
            estadistica.setProgreso(progreso);
            estadistica.setFechaActualizacion(LocalDateTime.now());
            return estadisticaRetoRepository.save(estadistica);
        }
        return null;
    }
}
