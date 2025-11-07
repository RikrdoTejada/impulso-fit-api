package com.impulsofit.controller;

import com.impulsofit.dto.request.AddProgresoRequestDTO;
import com.impulsofit.dto.request.RetoRequestDTO;
import com.impulsofit.dto.response.ProgresoResponseDTO;
import com.impulsofit.dto.response.RetoResponseDTO;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import com.impulsofit.model.Unidad;
import com.impulsofit.service.ParticipacionRetoService;
import com.impulsofit.service.RetoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import com.impulsofit.service.UnidadConverterService;
import com.impulsofit.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/retos")
@RequiredArgsConstructor
public class RetoController {

    private final RetoService retoService;
    private final UsuarioService usuarioService;
    private final ParticipacionRetoService participacionRetoService;
    private final UnidadConverterService unidadConverter;

    @PostMapping
public ResponseEntity<RetoResponseDTO> create(@Valid @RequestBody CrearRetoRequestDTO req) {
    RetoResponseDTO response = retoService.create(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

@GetMapping
public ResponseEntity<List<RetoResponseDTO>> findAll() {
    return ResponseEntity.ok(retoService.findAll());
}

@GetMapping("/busqueda/por-grupo/{id}")
public ResponseEntity<List<RetoResponseDTO>> findByGrupoId(@PathVariable("id") Long id) {
    return ResponseEntity.ok(retoService.findByGrupo_Id_grupo(id));
}

@GetMapping("/busqueda/por-creador/{id}")
public ResponseEntity<List<RetoResponseDTO>> findByCreadorId(@PathVariable("id") Long id) {
    return ResponseEntity.ok(retoService.findByCreador_Id(id));
}

@GetMapping("/busqueda/por-unidad/{id}")
public ResponseEntity<List<RetoResponseDTO>> findByUnitId(@PathVariable("id") Long id) {
    return ResponseEntity.ok(retoService.findByUnidad_IdUnidad(id));
}

@PutMapping("{id}")
public ResponseEntity<RetoResponseDTO> update(@PathVariable Long id, @RequestBody RetoRequestDTO r) {
    RetoResponseDTO updated = retoService.update(id, r);
    return ResponseEntity.ok(updated);
}

@DeleteMapping("{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    retoService.delete(id);
    return ResponseEntity.noContent().build();
}

    @PostMapping("/{idReto}/progreso/{idUsuario}")
    public ResponseEntity<ProgresoResponseDTO> agregarProgreso(
            @PathVariable Long idReto,
            @PathVariable Long idUsuario,
            @RequestBody AddProgresoRequestDTO request) {
        Optional<Reto> retoOpt = retoService.obtenerPorId(idReto);
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (retoOpt.isEmpty() || usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reto reto = retoOpt.get();

        // Validación por tipo de unidad: asegurar que el request sólo contenga campos permitidos
        validarCamposSegunUnidad(reto.getUnidad(), request);

        Double avanceEnBase = unidadConverter.convertirAUnidadBase(
                reto.getUnidad(),
                request.horas(),
                request.minutos(),
                request.kilometros(),
                request.metros(),
                request.cantidad()
        );

        if (avanceEnBase == null || avanceEnBase <= 0) {
            return ResponseEntity.badRequest().build();
        }

        if (unidadRequiereEntero(reto.getUnidad()) && !esEntero(avanceEnBase)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La unidad del reto no permite valores decimales. Envía un número entero.");
        }

        Double total = participacionRetoService.agregarProgreso(usuarioOpt.get(), reto, avanceEnBase);
        Double porcentaje = participacionRetoService.calcularPorcentajeForReto(reto, total);
        return ResponseEntity.ok(new ProgresoResponseDTO(idUsuario, idReto, total, porcentaje));
    }
    Double total = participacionRetoService.agregarProgreso(usuarioOpt.get(), retoOpt.get(), request.avance());
    Double porcentaje = participacionRetoService.calcularPorcentajeForReto(retoOpt.get(), total);
    return ResponseEntity.ok(new ProgresoResponseDTO(idUsuario, idReto, total, porcentaje));
}

    // Nuevo endpoint: alterna entre unirse y abandonar un reto
    @PostMapping("/{idReto}/participar/{idUsuario}")
    public ResponseEntity<Object> toggleParticipacion(@PathVariable Long idReto, @PathVariable Long idUsuario) {
        Optional<Reto> retoOpt = retoService.obtenerPorId(idReto);
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (retoOpt.isEmpty() || usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean joined = participacionRetoService.toggleParticipation(usuarioOpt.get(), retoOpt.get());
        if (joined) {
            return ResponseEntity.ok(Map.of("message", "Te has unido al reto"));
        } else {
            return ResponseEntity.ok(Map.of("message", "Has abandonado el reto, tu progreso ha sido descartado"));
        }
    }

    // Valida que el request contenga únicamente y al menos uno de los campos permitidos según la unidad
    private void validarCamposSegunUnidad(Unidad unidad, AddProgresoRequestDTO req) {
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


    // Determina si la unidad requiere valor entero (series, sesiones, puntos, entrenamiento/días)
    private boolean unidadRequiereEntero(Unidad unidad) {
        if (unidad == null) return false;
        String nombre = normalize(unidad.getNombre());
        String uso = normalize(unidad.getUso());
        if (nombre.contains("series") || nombre.contains("serie") || nombre.contains("sesion") || nombre.contains("sesiones")
                || nombre.contains("punto") || nombre.contains("puntos") || nombre.contains("entrenam")
                || uso.contains("repet") || uso.contains("sesion") || uso.contains("punto") || uso.contains("dia")) {
            return true;
        }
        return false;
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

    @GetMapping("/{idReto}/ranking")
    public ResponseEntity<List<ProgresoResponseDTO>> ranking(@PathVariable Long idReto) {
        Optional<Reto> retoOpt = retoService.obtenerPorId(idReto);
        if (retoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participacionRetoService.rankingDto(retoOpt.get()));
    }
}