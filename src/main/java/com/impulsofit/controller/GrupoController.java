package com.impulsofit.controller;

import com.impulsofit.dto.request.CrearGrupoRequestDTO;
import com.impulsofit.dto.response.GrupoResponseDTO;
import com.impulsofit.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/groups")
public class GrupoController {

    private final GrupoService service;

    public GrupoController(GrupoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GrupoResponseDTO> create(@Valid @RequestBody CrearGrupoRequestDTO req) {
        GrupoResponseDTO response = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}