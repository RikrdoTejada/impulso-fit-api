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
import java.util.stream.Collectors;

@Service
public class EstadisticaRetoService {

    private final ParticipacionRetoRepository participacionRetoRepository;
    private final RegistroProgresoRepository registroProgresoRepository;
    private final RetoRepository retoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadisticaRetoRepository estadisticaRetoRepository;

    public EstadisticaRetoService(ParticipacionRetoRepository participacionRetoRepository,
                                  RegistroProgresoRepository registroProgresoRepository,
                                  RetoRepository retoRepository,
                                  UsuarioRepository usuarioRepository,
                                  EstadisticaRetoRepository estadisticaRetoRepository) {
        this.participacionRetoRepository = participacionRetoRepository;
        this.registroProgresoRepository = registroProgresoRepository;
        this.retoRepository = retoRepository;
        this.usuarioRepository = usuarioRepository;
        this.estadisticaRetoRepository = estadisticaRetoRepository;
    }

    public DashboardRetoResponseDTO obtenerDashboardReto(Long idReto, Long idUsuario) {
        // Verificar si el usuario es participante del reto
        boolean esParticipante = participacionRetoRepository.existsByRetoIdRetoAndUsuarioIdUsuario(idReto, idUsuario);

        // Escenario Alternativo 1: No es participante
        if (!esParticipante) {
            return new DashboardRetoResponseDTO(false, false, "Debes unirte al reto antes de ver las estadísticas");
        }

        // Verificar si hay progresos registrados para este reto
        boolean tieneEstadisticas = registroProgresoRepository.existsByRetoIdReto(idReto);

        // Escenario Alternativo 2: No hay estadísticas
        if (!tieneEstadisticas) {
            return new DashboardRetoResponseDTO(true, false, "Aún no hay estadísticas disponibles para este reto");
        }

        // Escenario Exitoso: Obtener estadísticas completas
        return generarDashboardCompleto(idReto, idUsuario);
    }

    private DashboardRetoResponseDTO generarDashboardCompleto(Long idReto, Long idUsuario) {
        Reto reto = retoRepository.findById(idReto)
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        // Calcular total de días del reto
        long totalDiasReto = ChronoUnit.DAYS.between(reto.getFechaInicio(), reto.getFechaFin()) + 1;

        // Obtener participantes del reto
        List<Long> participantesIds = participacionRetoRepository.findParticipantesByRetoId(idReto);

        // Calcular estadísticas para cada participante
        List<EstadisticaRetoResponseDTO> estadisticas = calcularEstadisticasParticipantes(idReto, participantesIds, totalDiasReto);

        // Ordenar por porcentaje de cumplimiento (descendente) y asignar rankings
        estadisticas = asignarRankings(estadisticas);

        // Encontrar mi estadística y ranking
        EstadisticaRetoResponseDTO miEstadistica = estadisticas.stream()
                .filter(e -> e.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElse(null);

        Integer miRanking = miEstadistica != null ? miEstadistica.getRanking() : null;

        // Construir respuesta
        DashboardRetoResponseDTO dashboard = new DashboardRetoResponseDTO(true, true, "Estadísticas cargadas exitosamente");
        dashboard.setEstadisticasParticipantes(estadisticas);
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

            // Calcular días completados basado en RegistroProgreso
            Long diasCompletados = registroProgresoRepository.countByRetoIdRetoAndUsuarioIdUsuarioAndCompletadoTrue(idReto, participanteId);

            // Calcular puntos totales
            Integer puntosTotales = registroProgresoRepository.sumPuntosByRetoAndUsuario(idReto, participanteId);

            EstadisticaRetoResponseDTO estadistica = new EstadisticaRetoResponseDTO(
                    participanteId,
                    usuario.getNombre(),
                    diasCompletados,
                    totalDiasReto,
                    puntosTotales
            );

            estadisticas.add(estadistica);
        }

        return estadisticas;
    }

    private List<EstadisticaRetoResponseDTO> asignarRankings(List<EstadisticaRetoResponseDTO> estadisticas) {
        // Ordenar por porcentaje de cumplimiento (descendente) y luego por puntos
        estadisticas.sort((e1, e2) -> {
            int cmp = e2.getPorcentajeCumplimiento().compareTo(e1.getPorcentajeCumplimiento());
            if (cmp == 0) {
                return e2.getPuntosTotales().compareTo(e1.getPuntosTotales());
            }
            return cmp;
        });

        // Asignar rankings
        for (int i = 0; i < estadisticas.size(); i++) {
            estadisticas.get(i).setRanking(i + 1);
        }

        return estadisticas;
    }

    @Transactional
    public String registrarProgresoDiario(Long idReto, Long idUsuario, LocalDate fecha, boolean completado, Integer puntos) {
        // Verificar participación
        if (!participacionRetoRepository.existsByRetoIdRetoAndUsuarioIdUsuario(idReto, idUsuario)) {
            return "No participas en este reto";
        }

        Reto reto = retoRepository.findById(idReto)
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que la fecha esté dentro del rango del reto
        if (fecha.isBefore(reto.getFechaInicio()) || fecha.isAfter(reto.getFechaFin())) {
            return "La fecha está fuera del rango del reto";
        }

        // Buscar o crear registro de progreso
        RegistroProgreso progreso = registroProgresoRepository
                .findByRetoAndUsuarioAndFecha(reto, usuario, fecha)
                .orElse(new RegistroProgreso());

        progreso.setReto(reto);
        progreso.setUsuario(usuario);
        progreso.setFecha(fecha);
        progreso.setCompletado(completado);
        progreso.setPuntos(puntos != null ? puntos : (completado ? 10 : 0));
        progreso.setFechaRegistro(LocalDate.now());

        registroProgresoRepository.save(progreso);

        return completado ? "Día marcado como completado" : "Progreso registrado";
    }

    public Map<String, Object> obtenerMiProgreso(Long idReto, Long idUsuario) {
        if (!participacionRetoRepository.existsByRetoIdRetoAndUsuarioIdUsuario(idReto, idUsuario)) {
            throw new RuntimeException("No participas en este reto");
        }

        Reto reto = retoRepository.findById(idReto)
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        long totalDias = ChronoUnit.DAYS.between(reto.getFechaInicio(), reto.getFechaFin()) + 1;
        Long diasCompletados = registroProgresoRepository.countByRetoIdRetoAndUsuarioIdUsuarioAndCompletadoTrue(idReto, idUsuario);
        Integer puntosTotales = registroProgresoRepository.sumPuntosByRetoAndUsuario(idReto, idUsuario);
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