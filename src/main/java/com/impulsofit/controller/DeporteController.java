package com.impulsofit.controller;

import com.impulsofit.dto.request.DeporteRequest;
import com.impulsofit.dto.response.DeporteResponse;
import com.impulsofit.dto.response.DeporteResponseDTO;
import com.impulsofit.service.DeporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deportes")
@RequiredArgsConstructor
public class DeporteController {
    private final DeporteService deporteService;

    @GetMapping
    public ResponseEntity<List<DeporteResponseDTO>> listarDeportes() {
        return ResponseEntity.ok(deporteService.listarDeportesDTO());
    }

    @PostMapping
    public ResponseEntity<DeporteResponse> create(@RequestBody DeporteRequest d) {
        DeporteResponse saved = deporteService.create(d);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deporteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}