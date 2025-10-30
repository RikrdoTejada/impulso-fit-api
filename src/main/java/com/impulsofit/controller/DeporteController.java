package com.impulsofit.controller;

import com.impulsofit.dto.response.DeporteResponseDTO;
import com.impulsofit.model.Deporte;
import com.impulsofit.repository.DeporteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/deportes")
public class DeporteController {

    private final DeporteRepository deporteRepository;

    public DeporteController(DeporteRepository deporteRepository) {
        this.deporteRepository = deporteRepository;
    }

    @GetMapping
    public ResponseEntity<List<DeporteResponseDTO>> listarDeportes() {
        List<Deporte> deportes = deporteRepository.findAll();
        List<DeporteResponseDTO> dtos = deportes.stream()
                .map(d -> new DeporteResponseDTO(d.getIdDeporte(), d.getNombre(), d.getTipoDeporte()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
