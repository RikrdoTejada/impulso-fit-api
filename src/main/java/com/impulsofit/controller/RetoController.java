package com.impulsofit.controller;

import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import com.impulsofit.model.ParticipacionReto;
import com.impulsofit.model.EstadisticaReto;
import com.impulsofit.service.RetoService;
import com.impulsofit.service.UsuarioService;
import com.impulsofit.service.ParticipacionRetoService;
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

    @GetMapping
    public ResponseEntity<List<Reto>> listarRetos() {
        return ResponseEntity.ok(retoService.listarRetos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reto> obtenerReto(@PathVariable Long id) {
        Optional<Reto> reto = retoService.obtenerPorId(id);
        return reto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{idReto}/unirse/{idUsuario}")
    public ResponseEntity<ParticipacionReto> unirseAReto(
            @PathVariable Long idReto,
            @PathVariable Long idUsuario) {

        Optional<Reto> retoOpt = retoService.obtenerPorId(idReto);
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);

        if (retoOpt.isEmpty() || usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ParticipacionReto participacion = participacionRetoService.unirseAReto(usuarioOpt.get(), retoOpt.get());
        return ResponseEntity.ok(participacion);
    }

    @PostMapping("/{idReto}/participar/{idUsuario}")
    public ResponseEntity<EstadisticaReto> agregarProgreso(
            @PathVariable Long idReto,
            @PathVariable Long idUsuario,
            @RequestParam Double progreso) {

        Optional<Reto> retoOpt = retoService.obtenerPorId(idReto);
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);

        if (retoOpt.isEmpty() || usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EstadisticaReto estadistica = participacionRetoService.agregarProgreso(usuarioOpt.get(), retoOpt.get(), progreso);
        if (estadistica == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(estadistica);
    }

}
