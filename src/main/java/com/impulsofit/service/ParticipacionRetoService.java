package com.impulsofit.service;

import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.ParticipacionReto;
import com.impulsofit.model.RegistroProceso;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.ParticipacionRetoRepository;
import com.impulsofit.repository.RegistroProcesoRepository;
import com.impulsofit.dto.response.ProgresoResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParticipacionRetoService {

    private final ParticipacionRetoRepository participacionRetoRepository;

    private final RegistroProcesoRepository registroProcesoRepository;

    private final MembresiaGrupoRepository membresiaGrupoRepository;

    public ParticipacionRetoService(ParticipacionRetoRepository participacionRetoRepository,
                                    RegistroProcesoRepository registroProcesoRepository,
                                    MembresiaGrupoRepository membresiaGrupoRepository) {
        this.participacionRetoRepository = participacionRetoRepository;
        this.registroProcesoRepository = registroProcesoRepository;
        this.membresiaGrupoRepository = membresiaGrupoRepository;
    }

    @Transactional
    public Double agregarProgreso(Usuario usuario, Reto reto, Double avance) {
        if (avance == null || avance <= 0) {
            throw new IllegalArgumentException("El avance debe ser un número positivo");
        }

        ParticipacionReto participacion = participacionRetoRepository
                .findByRetoAndUsuario(reto, usuario)
                .orElseThrow(() -> new BusinessRuleException("El usuario no está participando en el reto"));

        RegistroProceso registro = new RegistroProceso();
        registro.setParticipacionReto(participacion);
        registro.setFecha(LocalDateTime.now());
        registro.setAvance(String.valueOf(avance));
        registroProcesoRepository.save(registro);

        return registroProcesoRepository.findByParticipacionRetoOrderByFechaDesc(participacion)
                .stream()
                .map(RegistroProceso::getAvance)
                .map(v -> { try { return Double.parseDouble(v); } catch (Exception e) { return 0.0; } })
                .reduce(0.0, Double::sum);
    }

    @Transactional
    public boolean toggleParticipation(Usuario usuario, Reto reto) {
        // Validar pertenencia al grupo del reto
        Long idUsuario = usuario.getIdUsuario();
        Long idGrupo = Optional.ofNullable(reto.getGrupo()).map(g -> g.getIdGrupo()).orElse(null);
        if (idGrupo == null) {
            throw new BusinessRuleException("El reto no tiene grupo asociado");
        }
        boolean miembro = membresiaGrupoRepository.existsByUsuario_IdUsuarioAndGrupo_IdGrupo(idUsuario, idGrupo);
        if (!miembro) {
            throw new BusinessRuleException("El usuario debe ingresar al grupo antes de participar en el reto");
        }

        Optional<ParticipacionReto> found = participacionRetoRepository.findByRetoAndUsuario(reto, usuario);
        if (found.isPresent()) {
            ParticipacionReto p = found.get();
            // Borrar todos los registros asociados a esta participacion
            List<RegistroProceso> registros = registroProcesoRepository.findByParticipacionRetoOrderByFechaDesc(p);
            if (!registros.isEmpty()) {
                registroProcesoRepository.deleteAll(registros);
            }
            participacionRetoRepository.delete(p);
            return false; // ha abandonado
        } else {
            ParticipacionReto p = new ParticipacionReto();
            p.setIdReto(reto.getIdReto());
            p.setIdUsuario(usuario.getIdUsuario());
            p.setUsuario(usuario);
            p.setReto(reto);
            p.setFechaUnion(LocalDateTime.now());
            participacionRetoRepository.save(p);
            return true; // se unió
        }
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
