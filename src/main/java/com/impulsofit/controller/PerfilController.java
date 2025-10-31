package com.impulsofit.controller;

import com.impulsofit.dto.request.PerfilRequestDTO;
import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.service.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> editarPerfil(@PathVariable Long id,
                                                          @Valid @RequestBody PerfilRequestDTO request) {
        PerfilResponseDTO actualizado = perfilService.actualizarPerfil(id, request);
        return ResponseEntity.ok(actualizado);
    }
}
