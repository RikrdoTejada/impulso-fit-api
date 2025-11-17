package com.impulsofit.controller;

import com.impulsofit.dto.request.AddProgresoRequestDTO;
import com.impulsofit.dto.request.RetoRequestDTO;
import com.impulsofit.dto.response.ProgresoResponseDTO;
import com.impulsofit.dto.response.RetoResponseDTO;
import com.impulsofit.model.Perfil;
import com.impulsofit.model.Reto;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.service.ParticipacionRetoService;
import com.impulsofit.service.RetoService;
import com.impulsofit.service.UnidadConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/retos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class RetoController {

    private final RetoService retoService;
    private final ParticipacionRetoService participacionRetoService;
    private final UnidadConverterService unidadConverter;
    private final PerfilRepository perfilRepository;

    @PostMapping
    public ResponseEntity<RetoResponseDTO> create(@RequestBody RetoRequestDTO r) {
        RetoResponseDTO saved = retoService.create(r);
        return ResponseEntity.ok(saved);
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

    @PostMapping("/{idReto}/progreso/{idPerfil}")
    public ResponseEntity<ProgresoResponseDTO> agregarProgreso(
            @PathVariable Long idReto,
            @PathVariable Long idPerfil,
            @RequestBody AddProgresoRequestDTO request) {
        Optional<Reto> retoOpt = retoService.obtenerPorId(idReto);
        Optional<Perfil> perfilOpt = perfilRepository.findById(idPerfil);
        if (retoOpt.isEmpty() || perfilOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reto reto = retoOpt.get();

        // Delegar validación al servicio
        participacionRetoService.validarCamposSegunUnidad(reto.getUnidad(), request);

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

        // Delegar comprobación de entero al servicio
        participacionRetoService.validarValorEnteroSiCorresponde(reto.getUnidad(), avanceEnBase);

        Double total = participacionRetoService.agregarProgreso(perfilOpt.get(), reto, avanceEnBase);
        Double porcentaje = participacionRetoService.calcularPorcentajeForReto(reto, total);
        return ResponseEntity.ok(new ProgresoResponseDTO(idPerfil, idReto, total, porcentaje));
    }

    @PostMapping("/{idReto}/participar/{idPerfil}")
    public ResponseEntity<Object> toggleParticipacion(@PathVariable Long idReto, @PathVariable Long idPerfil) {
        Optional<Reto> retoOpt = retoService.obtenerPorId(idReto);
        Optional<Perfil> perfilOpt = perfilRepository.findById(idPerfil);
        if (retoOpt.isEmpty() || perfilOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        boolean joined = participacionRetoService.toggleParticipation(perfilOpt.get(), retoOpt.get());
        return ResponseEntity.ok(Map.of("message", joined ? "Te has unido al reto" : "Has abandonado el reto, tu progreso ha sido descartado"));
    }


    @GetMapping("/{idReto}/ranking")
    public ResponseEntity<List<ProgresoResponseDTO>> ranking(@PathVariable Long idReto) {
        return retoService.obtenerPorId(idReto)
                .map(reto -> ResponseEntity.ok(participacionRetoService.rankingDto(reto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}