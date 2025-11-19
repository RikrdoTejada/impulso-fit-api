package com.impulsofit.controller;

import com.impulsofit.dto.request.DeporteRequestDTO;
import com.impulsofit.dto.response.DeporteResponseDTO;
import com.impulsofit.service.DeporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deportes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DeporteController {
    private final DeporteService deporteService;

    @GetMapping
    public ResponseEntity<List<DeporteResponseDTO>> listarDeportes() {
        return ResponseEntity.ok(deporteService.listarDeportesDTO());
    }

    @PostMapping
    public ResponseEntity<DeporteResponseDTO> create(@RequestBody DeporteRequestDTO d) {
        DeporteResponseDTO saved = deporteService.create(d);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deporteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}