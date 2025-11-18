package com.impulsofit.service;

import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.*;
import com.impulsofit.repository.ParticipacionRetoRepository;
import com.impulsofit.repository.RegistroProcesoRepository;
import com.impulsofit.dto.response.ProgresoResponseDTO;
import com.impulsofit.dto.request.AddProgresoRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParticipacionRetoService {

    private final ParticipacionRetoRepository participacionRetoRepository;

    private final RegistroProcesoRepository registroProcesoRepository;

    private final MembresiaGrupoService membresiaGrupoService;

    public ParticipacionRetoService(ParticipacionRetoRepository participacionRetoRepository,
                                    RegistroProcesoRepository registroProcesoRepository,
                                    MembresiaGrupoService membresiaGrupoService) {
        this.participacionRetoRepository = participacionRetoRepository;
        this.registroProcesoRepository = registroProcesoRepository;
        this.membresiaGrupoService = membresiaGrupoService;
    }

    @Transactional
    public Double agregarProgreso(Perfil perfil, Reto reto, Double avance) {
        if (avance == null || avance <= 0) {
            throw new IllegalArgumentException("El avance debe ser un número positivo");
        }

        ParticipacionReto participacion = participacionRetoRepository
                .findByRetoAndPerfil(reto, perfil)
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
    public boolean toggleParticipation(Perfil perfil, Reto reto) {
        // Validar pertenencia al grupo del reto
        Long idUsuario = perfil.getIdPerfil();
        Long idGrupo = Optional.ofNullable(reto.getGrupo()).map(Grupo::getIdGrupo).orElse(null);
        membresiaGrupoService.validarUsuarioEsMiembro(idUsuario, idGrupo);

        Optional<ParticipacionReto> found = participacionRetoRepository.findByRetoAndPerfil(reto, perfil);
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
            p.setIdPerfil(perfil.getIdPerfil());
            p.setPerfil(perfil);
            p.setReto(reto);
            p.setFechaUnion(LocalDateTime.now());
            participacionRetoRepository.save(p);
            return true; // se unió
        }
    }

    public List<UsuarioTotal> rankingPorReto(Reto reto) {
        Map<Long, Double> sumas = new HashMap<>();
        for (RegistroProceso r : registroProcesoRepository.findByParticipacionReto_Reto(reto)) {
            Long idPerfil = Optional.ofNullable(r.getParticipacionReto()).map(ParticipacionReto::getPerfil).map(Perfil::getIdPerfil).orElse(null);
            if (idPerfil     == null) continue;
            double val = 0.0;
            try { val = Double.parseDouble(Optional.ofNullable(r.getAvance()).orElse("0")); } catch (Exception ignored) {}
            sumas.merge(idPerfil, val, Double::sum);
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

    public void validarCamposSegunUnidad(Unidad unidad, AddProgresoRequestDTO req) {
        if (unidad == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unidad del reto no definida");
        }
        String nombre = normalize(unidad.getNombre());
        String uso = normalize(unidad.getUso());

        boolean isTiempo = nombre.contains("tiempo") || uso.contains("min");
        boolean isDistancia = nombre.contains("distancia") || uso.contains("metro") || uso.contains("kil");
        boolean isPeso = nombre.contains("peso") || uso.contains("kilogr");
        boolean isEntrenamiento = nombre.contains("entren") || uso.contains("dia");
        boolean isSeries = uso.contains("repet") || nombre.contains("series") || nombre.contains("serie");
        boolean isSesiones = uso.contains("sesion") || nombre.contains("sesion") || nombre.contains("sesiones");
        boolean isPuntos = uso.contains("punto") || nombre.contains("punto") || nombre.contains("puntos");

        if (isTiempo) {
            boolean tieneCompuesto = req.horas() != null || req.minutos() != null;
            if (!tieneCompuesto) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unidad incorrecta: para retos de tiempo envía horas y/o minutos.");
            }
            if (req.kilometros() != null || req.metros() != null || req.series() != null || req.sesiones() != null
                    || req.puntos() != null || req.dias() != null || req.kilogramos() != null || req.cantidad() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Campos inválidos: para retos de tiempo solo envía horas y/o minutos.");
            }
            return;
        }

        if (isDistancia) {
            boolean tieneCompuesto = req.kilometros() != null || req.metros() != null;
            if (!tieneCompuesto) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unidad incorrecta: para retos de distancia envía kilometros y/o metros.");
            }
            if (req.horas() != null || req.minutos() != null || req.series() != null || req.sesiones() != null
                    || req.puntos() != null || req.dias() != null || req.kilogramos() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Campos inválidos: para retos de distancia solo envía kilometros y/o metros.");
            }
            return;
        }

        if (isPeso) {
            if (req.kilogramos() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unidad incorrecta: para retos de peso envía kilogramos.");
            }
            if (req.horas() != null || req.minutos() != null || req.kilometros() != null || req.metros() != null
                    || req.series() != null || req.sesiones() != null || req.puntos() != null || req.dias() != null
                    || req.cantidad() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Campos inválidos: para retos de peso solo envía kilogramos.");
            }
            return;
        }

        if (isSeries) {
            if (req.series() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unidad incorrecta: para retos de series envía el campo 'series' (entero).");
            }
            if (req.horas() != null || req.minutos() != null || req.kilometros() != null || req.metros() != null
                    || req.sesiones() != null || req.puntos() != null || req.dias() != null || req.kilogramos() != null
                    || req.cantidad() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Campos inválidos: para retos de series solo envía el campo 'series'.");
            }
            return;
        }

        if (isSesiones) {
            if (req.sesiones() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unidad incorrecta: para retos de sesiones envía el campo 'sesiones' (entero).");
            }
            return;
        }

        if (isPuntos) {
            if (req.puntos() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unidad incorrecta: para retos de puntos envía el campo 'puntos' (entero).");
            }
            return;
        }

        if (isEntrenamiento) {
            if (req.dias() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unidad incorrecta: para retos de entrenamiento envía el campo 'dias' (entero).");
            }
            return;
        }

        // Fallback: permitir avance o cantidad si no se reconoce la unidad
        if (req.cantidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Enviar un valor de avance apropiado para este tipo de reto.");
        }
    }

    public boolean unidadRequiereEntero(Unidad unidad) {
        if (unidad == null) return false;

        String nombre = normalize(unidad.getNombre());
        String uso = normalize(unidad.getUso());

        return nombre.contains("series") || nombre.contains("serie") || nombre.contains("sesion")
                || nombre.contains("sesiones") || nombre.contains("punto") || nombre.contains("puntos")
                || nombre.contains("entrenam") || uso.contains("repet") || uso.contains("sesion")
                || uso.contains("punto") || uso.contains("dia");
    }

    public void validarValorEnteroSiCorresponde(Unidad unidad, Double v) {
        if (unidadRequiereEntero(unidad) && !esEntero(v)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La unidad del reto no permite valores decimales. Envía un número entero.");
        }
    }

    private String normalize(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return n.toLowerCase();
    }

    private boolean esEntero(Double v) {
        if (v == null) return false;
        double rounded = Math.round(v);
        return Math.abs(v - rounded) < 1e-9;
    }

    public record UsuarioTotal(Long idUsuario, Double total) {}
}
