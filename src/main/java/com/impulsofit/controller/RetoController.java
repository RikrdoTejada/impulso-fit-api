package com.impulsofit.controller;

import com.impulsofit.model.Reto;
import com.impulsofit.service.RetoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RetoController {

    private final RetoService retoService;

    public RetoController(RetoService retoService) {
        this.retoService = retoService;
    }

    // Unirse a reto
    @PostMapping("/retos/{idReto}/unirse/{idUsuario}")
    public String unirseAReto(@PathVariable Long idReto, @PathVariable Long idUsuario) {
        return retoService.unirseAReto(idUsuario, idReto);
    }

    // Abandonar reto
    @PostMapping("/retos/{idReto}/abandonar/{idUsuario}")
    public String abandonarReto(@PathVariable Long idReto, @PathVariable Long idUsuario) {
        return retoService.abandonarReto(idUsuario, idReto);
    }

    // Verificar participación
    @GetMapping("/retos/{idReto}/participa/{idUsuario}")
    public boolean participaEnReto(@PathVariable Long idReto, @PathVariable Long idUsuario) {
        return retoService.participaEnReto(idUsuario, idReto);
    }

    // Obtener retos de un grupo
    @GetMapping("/grupos/{idGrupo}/retos")
    public List<Reto> obtenerRetosDeGrupo(@PathVariable Long idGrupo) {
        return retoService.obtenerRetosDeGrupo(idGrupo);
    }

    // Crear reto de prueba
    @PostMapping("/retos-test/{idGrupo}")
    public String crearRetoDePrueba(@PathVariable Long idGrupo) {
        return retoService.crearRetoDePrueba(idGrupo);
    }
}