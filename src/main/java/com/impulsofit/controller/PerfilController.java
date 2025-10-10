package com.impulsofit.controller;

import com.impulsofit.model.Perfil;
import com.impulsofit.service.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Perfil> editarPerfil(@PathVariable Long id, @RequestBody Perfil datosActualizados) {
        return perfilService.actualizarPerfil(id, datosActualizados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
