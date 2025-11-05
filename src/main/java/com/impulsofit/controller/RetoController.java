package com.impulsofit.controller;

import com.impulsofit.dto.request.CrearRetoRequestDTO;
import com.impulsofit.dto.response.RetoResponseDTO;
import com.impulsofit.service.RetoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/challenges")
public class RetoController {

    private final RetoService service;

    public RetoController(RetoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RetoResponseDTO> create(@Valid @RequestBody CrearRetoRequestDTO req) {
        RetoResponseDTO response = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}