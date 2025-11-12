package com.impulsofit.controller;

import com.impulsofit.dto.request.PerfilRequestDTO;
import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/perfil")
public class PerfilController {

    private final PerfilService perfilService;

    @PostMapping
    public ResponseEntity<PerfilResponseDTO> crearPerfil(@RequestBody PerfilRequestDTO req){
        PerfilResponseDTO saved = perfilService.crearPerfil(req);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> editarPerfil(@PathVariable Long id,
                                                          @RequestBody PerfilRequestDTO request) {
        PerfilResponseDTO actualizado = perfilService.actualizarPerfil(id, request);
        return ResponseEntity.ok(actualizado);
    }
}
