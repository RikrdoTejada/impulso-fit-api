package com.impulsofit.controller;

import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.service.AuthService;
import com.impulsofit.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AuthService authService;


    @PutMapping("/update/cred/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateCred(@PathVariable Long id, @RequestBody RecoverRequestDTO r) {
        return ResponseEntity.ok(usuarioService.updateCred(id, r));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        //authService.delete(id);
        return ResponseEntity.noContent().build();
    }
}