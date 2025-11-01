package com.impulsofit.controller;

import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.request.UsuarioRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioRequestDTO u) {
        UsuarioResponseDTO saved = usuarioService.create(u);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update/cred/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateCred(@PathVariable Long id, @RequestBody RecoverRequestDTO r) {
        return ResponseEntity.ok(usuarioService.updateCred(id, r));
    }

    @PutMapping("/update/info/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateInfo(@PathVariable Long id, @RequestBody UsuarioRequestDTO u) {
        return ResponseEntity.ok(usuarioService.updateInfo(id, u));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}