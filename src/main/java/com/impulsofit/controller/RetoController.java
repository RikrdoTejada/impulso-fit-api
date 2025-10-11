package com.impulsofit.controller;

import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
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

    // Obtener todos los retos
    @GetMapping
    public ResponseEntity<List<Reto>> listarRetos() {
        List<Reto> retos = retoService.listarRetos();
        return ResponseEntity.ok(retos);
    }

    // Crear un reto
    @PostMapping
    public ResponseEntity<Reto> crearReto(@RequestBody Reto reto) {
        Reto nuevoReto = retoService.guardar(reto);
        return ResponseEntity.ok(nuevoReto);
    }

    // Obtener un reto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Reto> obtenerReto(@PathVariable Long id) {
        Optional<Reto> reto = retoService.obtenerPorId(id);
        return reto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un reto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReto(@PathVariable Long id) {
        retoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Unirse a un reto (solo si el usuario existe)
    @PostMapping("/{idReto}/unirse/{idUsuario}")
    public ResponseEntity<String> unirseAReto(
            @PathVariable Long idReto,
            @PathVariable Long idUsuario) {

        Optional<Reto> retoOpt = retoService.obtenerPorId(idReto);
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);

        if (retoOpt.isEmpty() || usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Aquí podrías registrar la participación si tienes una entidad ParticipacionReto más adelante
        return ResponseEntity.ok("Usuario " + idUsuario + " se ha unido al reto " + idReto);
    }
}
