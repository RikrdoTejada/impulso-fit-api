package com.impulsofit.controller;

import com.impulsofit.dto.request.AddProgresoRequestDTO;
import com.impulsofit.dto.response.ProgresoResponseDTO;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import com.impulsofit.service.ParticipacionRetoService;
import com.impulsofit.service.RetoService;
import com.impulsofit.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/retos")
public class RetoController {

    @Autowired
    private RetoService retoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ParticipacionRetoService participacionRetoService;

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
