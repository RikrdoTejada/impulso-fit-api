package com.impulsofit.service;

import com.impulsofit.model.ParticipacionReto;
import com.impulsofit.model.RegistroProceso;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.ParticipacionRetoRepository;
import com.impulsofit.repository.RegistroProcesoRepository;
import com.impulsofit.dto.response.ProgresoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParticipacionRetoService {

    @Autowired
    private ParticipacionRetoRepository participacionRetoRepository;

    @Autowired
    private RegistroProcesoRepository registroProcesoRepository;

    @Transactional
    public Double agregarProgreso(Usuario usuario, Reto reto, Double avance) {
        if (avance == null || avance <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El avance debe ser un número positivo");
        }

        ParticipacionReto participacion = participacionRetoRepository
                .findByRetoAndUsuario(reto, usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no está participando en el reto"));

        RegistroProceso registro = new RegistroProceso();
        registro.setParticipacionReto(participacion);
        registro.setFecha(LocalDateTime.now());
        registro.setAvance(String.valueOf(avance));
        registroProcesoRepository.save(registro);

        double total = registroProcesoRepository.findByParticipacionRetoOrderByFechaDesc(participacion)
                .stream()
                .map(RegistroProceso::getAvance)
                .map(v -> { try { return Double.parseDouble(v); } catch (Exception e) { return 0.0; } })
                .reduce(0.0, Double::sum);
        return total;
    }

    public List<UsuarioTotal> rankingPorReto(Reto reto) {
        Map<Long, Double> sumas = new HashMap<>();
        for (RegistroProceso r : registroProcesoRepository.findByParticipacionReto_Reto(reto)) {
            Long idUsuario = Optional.ofNullable(r.getParticipacionReto()).map(ParticipacionReto::getIdUsuario).orElse(null);
            if (idUsuario == null) continue;
            double val = 0.0;
            try { val = Double.parseDouble(Optional.ofNullable(r.getAvance()).orElse("0")); } catch (Exception ignored) {}
            sumas.merge(idUsuario, val, Double::sum);
        }
        return sumas.entrySet().stream()
                .sorted((a,b) -> Double.compare(b.getValue(), a.getValue()))
                .map(e -> new UsuarioTotal(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    // Nuevo: transformar el ranking a DTOs listos para el controlador
    public List<ProgresoResponseDTO> rankingDto(Reto reto) {
        Double objetivo = reto.getObjetivoTotal();
        return rankingPorReto(reto).stream()
                .map(up -> new ProgresoResponseDTO(up.idUsuario(), reto.getIdReto(), up.total(), calcularPorcentaje(objetivo, up.total())))
                .collect(Collectors.toList());
    }

    public Double calcularPorcentajeForReto(Reto reto, Double total) {
        return calcularPorcentaje(reto.getObjetivoTotal(), total);
    }

    private Double calcularPorcentaje(Double objetivo, Double total) {
        if (objetivo == null || objetivo <= 0 || total == null) return null;
        double p = (total / objetivo) * 100.0;
        if (p < 0) p = 0;
        if (p > 100) p = 100;
        return Math.round(p * 100.0) / 100.0; // 2 decimales
    }

    public record UsuarioTotal(Long idUsuario, Double total) {}
}
