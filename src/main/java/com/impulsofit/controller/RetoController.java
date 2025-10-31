package com.impulsofit.controller;

import com.impulsofit.dto.request.AddProgresoRequestDTO;
import com.impulsofit.dto.request.RetoRequest;
import com.impulsofit.dto.response.ProgresoResponseDTO;
import com.impulsofit.dto.response.RetoResponse;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import com.impulsofit.service.ParticipacionRetoService;
import com.impulsofit.service.RetoService;
import com.impulsofit.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/retos")
@RequiredArgsConstructor
public class RetoController {

    private final RetoService retoService;
    private final UsuarioService usuarioService;
    private final ParticipacionRetoService participacionRetoService;

    @PostMapping
    public ResponseEntity<RetoResponse> create(@RequestBody RetoRequest r) {
        RetoResponse saved = retoService.create(r);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<RetoResponse>> findAll() {
        return ResponseEntity.ok(retoService.findAll());
    }

    @GetMapping("/search/by-group/{id}")
    public ResponseEntity<List<RetoResponse>> findByGrupoId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(retoService.findByGrupo_Id_grupo(id));
    }

    @GetMapping("/search/by-creator/{id}")
    public ResponseEntity<List<RetoResponse>> findByCreadorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(retoService.findByCreador_Id(id));
    }

    @GetMapping("/search/by-unit/{id}")
    public ResponseEntity<List<RetoResponse>> findByUnitId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(retoService.findByUnidad_IdUnidad(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<RetoResponse> update(@PathVariable Long id, @RequestBody RetoRequest r) {
        RetoResponse updated = retoService.update(id, r);
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
        Double total = participacionRetoService.agregarProgreso(usuarioOpt.get(), retoOpt.get(), request.getAvance());
        Double porcentaje = participacionRetoService.calcularPorcentajeForReto(retoOpt.get(), total);
        return ResponseEntity.ok(new ProgresoResponseDTO(idUsuario, idReto, total, porcentaje));
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