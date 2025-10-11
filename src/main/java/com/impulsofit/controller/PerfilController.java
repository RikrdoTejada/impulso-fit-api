package com.impulsofit.controller;

import com.impulsofit.dto.request.PerfilRequest;
import com.impulsofit.dto.response.PerfilResponse;
import com.impulsofit.service.PerfilService;
import com.impulsofit.exception.BusinessRuleException;
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
    public ResponseEntity<PerfilResponse> editarPerfil(@PathVariable Long id,
                                                       @RequestBody PerfilRequest request) {
        try {
            PerfilResponse actualizado = perfilService.actualizarPerfil(id, request);
            return ResponseEntity.ok(actualizado);
        } catch (BusinessRuleException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
