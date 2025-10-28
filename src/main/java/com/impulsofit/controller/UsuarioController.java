package com.impulsofit.controller;

import com.impulsofit.dto.request.RecoverRequest;
import com.impulsofit.dto.request.UsuarioRequest;
import com.impulsofit.dto.response.UsuarioResponse;
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
    public ResponseEntity<UsuarioResponse> create(@RequestBody UsuarioRequest u) {
        UsuarioResponse saved = usuarioService.create(u);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/update/cred/{id}")
    public ResponseEntity<UsuarioResponse> updateCred(@PathVariable Long id, @RequestBody RecoverRequest r) {
        return ResponseEntity.ok(usuarioService.updateCred(id, r));
    }

    @PutMapping("/update/info/{id}")
    public ResponseEntity<UsuarioResponse> updateInfo(@PathVariable Long id, @RequestBody UsuarioRequest u) {
        return ResponseEntity.ok(usuarioService.updateInfo(id, u));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}