package com.impulsofit.controller;

import com.impulsofit.dto.response.DeporteResponseDTO;
import com.impulsofit.service.DeporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deportes")
public class DeporteController {

    private final DeporteService deporteService;

    public DeporteController(DeporteService deporteService) {
        this.deporteService = deporteService;
    }

    @GetMapping
    public ResponseEntity<List<DeporteResponseDTO>> listarDeportes() {
        return ResponseEntity.ok(deporteService.listarDeportesDTO());
    }
}
