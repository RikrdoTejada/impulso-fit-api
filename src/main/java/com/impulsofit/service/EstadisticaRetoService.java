package com.impulsofit.service;

import com.impulsofit.dto.response.DashboardRetoResponseDTO;
import com.impulsofit.dto.response.EstadisticaRetoResponseDTO;
import com.impulsofit.model.*;
import com.impulsofit.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class EstadisticaRetoService {

    private final ParticipacionRetoRepository participacionRetoRepository;
    private final RegistroProcesoRepository registroProcesoRepository;
    private final RetoRepository retoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadisticaRetoRepository estadisticaRetoRepository;

    public EstadisticaRetoService(ParticipacionRetoRepository participacionRetoRepository,
                                  RegistroProcesoRepository registroProcesoRepository,
                                  RetoRepository retoRepository,
                                  UsuarioRepository usuarioRepository,
                                  EstadisticaRetoRepository estadisticaRetoRepository) {
        this.participacionRetoRepository = participacionRetoRepository;
        this.registroProcesoRepository = registroProcesoRepository;
        this.retoRepository = retoRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadisticaRetoRepository = estadisticaRetoRepository;
    }

    public DashboardRetoResponseDTO obtenerDashboardReto(Long idReto, Long idUsuario) {
        boolean esParticipante = participacionRetoRepository.existsByRetoIdRetoAndUsuarioIdUsuario(idReto, idUsuario);

        if (!esParticipante) {
            return new DashboardRetoResponseDTO(false, false, "Debes unirte al reto antes de ver las estadísticas");
        }

        boolean tieneEstadisticas = registroProcesoRepository.existsByRetoIdReto(idReto);

        if (!tieneEstadisticas) {
            return new DashboardRetoResponseDTO(true, false, "Aún no hay estadísticas disponibles para este reto");
        }

        return generarDashboardCompleto(idReto, idUsuario);
    }

    private DashboardRetoResponseDTO generarDashboardCompleto(Long idReto, Long idUsuario) {
        Reto reto = retoRepository.findById(idReto)
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        long totalDiasReto = ChronoUnit.DAYS.between(reto.getFechaInicio(), reto.getFechaFin()) + 1;
        List<Long> participantesIds = participacionRetoRepository.findParticipantesByRetoId(idReto);
        List<EstadisticaRetoResponseDTO> estadisticas = calcularEstadisticasParticipantes(idReto, participantesIds, totalDiasReto);
        List<EstadisticaRetoResponseDTO> estadisticasRankeadas = asignarRankings(estadisticas);

        EstadisticaRetoResponseDTO miEstadistica = estadisticasRankeadas.stream()
                .filter(e -> Objects.equals(e.idUsuario(), idUsuario))
                .findFirst()
                .orElse(null);

        Integer miRanking = miEstadistica != null ? miEstadistica.ranking() : null;

        DashboardRetoResponseDTO dashboard = new DashboardRetoResponseDTO(true, true, "Estadísticas cargadas exitosamente");
        dashboard.setEstadisticasParticipantes(estadisticasRankeadas);
        dashboard.setMiEstadistica(miEstadistica);
        dashboard.setMiRanking(miRanking);
        dashboard.setTotalParticipantes((long) participantesIds.size());

        return dashboard;
    }

    private List<EstadisticaRetoResponseDTO> calcularEstadisticasParticipantes(Long idReto, List<Long> participantesIds, long totalDiasReto) {
        List<EstadisticaRetoResponseDTO> estadisticas = new ArrayList<>();

        for (Long participanteId : participantesIds) {
            Usuario usuario = usuarioRepository.findById(participanteId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Long diasCompletados = registroProcesoRepository.countByRetoIdRetoAndUsuarioIdUsuarioAndCompletadoTrue(idReto, participanteId);
            Integer puntosTotales = registroProcesoRepository.sumPuntosByRetoAndUsuario(idReto, participanteId);
            Double porcentajeCumplimiento = totalDiasReto > 0 ? (diasCompletados * 100.0) / totalDiasReto : 0.0;

            EstadisticaRetoResponseDTO estadistica = new EstadisticaRetoResponseDTO(
                    participanteId,
                    usuario.getNombreCompleto(),
                    diasCompletados,
                    totalDiasReto,
                    porcentajeCumplimiento,
                    puntosTotales != null ? puntosTotales : 0,
                    null
            );

            estadisticas.add(estadistica);
        }

        return estadisticas;
    }

    private List<EstadisticaRetoResponseDTO> asignarRankings(List<EstadisticaRetoResponseDTO> estadisticas) {
        estadisticas.sort((e1, e2) -> {
            int cmp = e2.porcentajeCumplimiento().compareTo(e1.porcentajeCumplimiento());
            if (cmp == 0) {
                return e2.puntosTotales().compareTo(e1.puntosTotales());
            }
            return cmp;
        });

        List<EstadisticaRetoResponseDTO> estadisticasRankeadas = new ArrayList<>();
        for (int i = 0; i < estadisticas.size(); i++) {
            EstadisticaRetoResponseDTO original = estadisticas.get(i);
            EstadisticaRetoResponseDTO conRanking = new EstadisticaRetoResponseDTO(
                    original.idUsuario(),
                    original.nombreUsuario(),
                    original.diasCompletados(),
                    original.totalDias(),
                    original.porcentajeCumplimiento(),
                    original.puntosTotales(),
                    i + 1
            );
            estadisticasRankeadas.add(conRanking);
        }

        return estadisticasRankeadas;
    }

    @Transactional
    public String registrarProgresoDiario(Long idReto, Long idUsuario, LocalDate fecha, boolean completado, Integer puntos) {
        if (!participacionRetoRepository.existsByRetoIdRetoAndUsuarioIdUsuario(idReto, idUsuario)) {
            return "No participas en este reto";
        }

        Reto reto = retoRepository.findById(idReto)
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (fecha.isBefore(reto.getFechaInicio()) || fecha.isAfter(reto.getFechaFin())) {
            return "La fecha está fuera del rango del reto";
        }

        RegistroProceso progreso = registroProcesoRepository
                .findByRetoAndUsuarioAndFecha(reto, usuario, fecha)
                .orElse(new RegistroProceso());

        progreso.setReto(reto);
        progreso.setUsuario(usuario);
        progreso.setFecha(fecha);
        progreso.setCompletado(completado);
        progreso.setPuntos(puntos != null ? puntos : (completado ? 10 : 0));
        progreso.setFechaRegistro(LocalDate.now());

        registroProcesoRepository.save(progreso);

        return completado ? "Día marcado como completado" : "Progreso registrado";
    }

    public Map<String, Object> obtenerMiProgreso(Long idReto, Long idUsuario) {
        if (!participacionRetoRepository.existsByRetoIdRetoAndUsuarioIdUsuario(idReto, idUsuario)) {
            throw new RuntimeException("No participas en este reto");
        }

        Reto reto = retoRepository.findById(idReto)
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        long totalDias = ChronoUnit.DAYS.between(reto.getFechaInicio(), reto.getFechaFin()) + 1;
        Long diasCompletados = registroProcesoRepository.countByRetoIdRetoAndUsuarioIdUsuarioAndCompletadoTrue(idReto, idUsuario);
        Integer puntosTotales = registroProcesoRepository.sumPuntosByRetoAndUsuario(idReto, idUsuario);
        double porcentaje = totalDias > 0 ? (diasCompletados * 100.0) / totalDias : 0;

        Map<String, Object> progreso = new HashMap<>();
        progreso.put("diasCompletados", diasCompletados);
        progreso.put("totalDias", totalDias);
        progreso.put("porcentaje", Math.round(porcentaje * 100.0) / 100.0);
        progreso.put("diasRestantes", totalDias - diasCompletados);
        progreso.put("puntosTotales", puntosTotales != null ? puntosTotales : 0);

        return progreso;
    }
}