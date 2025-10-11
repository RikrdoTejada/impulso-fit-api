package com.impulsofit.controller;

import com.impulsofit.service.GrupoMembresiaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class GrupoMembresiaController {

    private final GrupoMembresiaService grupoMembresiaService;

    public GrupoMembresiaController(GrupoMembresiaService grupoMembresiaService) {
        this.grupoMembresiaService = grupoMembresiaService;
    }

    // Unirse a grupo
    @PostMapping("/grupos/{idGrupo}/unirse/{idUsuario}")
    public String unirseAGrupo(@PathVariable Long idGrupo, @PathVariable Long idUsuario) {
        return grupoMembresiaService.unirseAGrupo(idUsuario, idGrupo);
    }

    // Dejar grupo
    @PostMapping("/grupos/{idGrupo}/dejar/{idUsuario}")
    public String dejarGrupo(@PathVariable Long idGrupo, @PathVariable Long idUsuario) {
        return grupoMembresiaService.dejarGrupo(idUsuario, idGrupo);
    }

    // Verificar membresía
    @GetMapping("/grupos/{idGrupo}/es-miembro/{idUsuario}")
    public boolean esMiembroDeGrupo(@PathVariable Long idGrupo, @PathVariable Long idUsuario) {
        return grupoMembresiaService.esMiembroDeGrupo(idUsuario, idGrupo);
    }
}