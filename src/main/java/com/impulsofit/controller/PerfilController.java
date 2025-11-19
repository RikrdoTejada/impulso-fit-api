package com.impulsofit.controller;

import com.impulsofit.dto.request.CredentialsRequestDTO;
import com.impulsofit.dto.request.PerfilRequestDTO;
import com.impulsofit.dto.response.AuthResponseDTO;
import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/perfil")
@PreAuthorize("hasRole('USER')")
public class PerfilController {

    private final PerfilService perfilService;

    @PostMapping
    public ResponseEntity<PerfilResponseDTO> crearPerfil(@RequestBody PerfilRequestDTO req){
        PerfilResponseDTO saved = perfilService.crearPerfil(req);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<PerfilResponseDTO> editarPerfil(@PathVariable Long id,
                                                          @RequestBody PerfilRequestDTO request) {
        PerfilResponseDTO actualizado = perfilService.actualizarPerfil(id, request);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/editar/cred/")
    public ResponseEntity<AuthResponseDTO> editarCred(@Valid @RequestBody CredentialsRequestDTO r) {
        return ResponseEntity.ok(perfilService.actualizarCred(r));
    }
}
