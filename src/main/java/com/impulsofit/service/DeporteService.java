package com.impulsofit.service;

import com.impulsofit.dto.response.DeporteResponseDTO;
import com.impulsofit.model.Deporte;
import com.impulsofit.repository.DeporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeporteService {

    private final DeporteRepository deporteRepository;

    public DeporteService(DeporteRepository deporteRepository) {
        this.deporteRepository = deporteRepository;
    }

    public List<DeporteResponseDTO> listarDeportesDTO() {
        List<Deporte> deportes = deporteRepository.findAll();
        return deportes.stream()
                .map(d -> new DeporteResponseDTO(d.getIdDeporte(), d.getNombre(), d.getTipoDeporte()))
                .collect(Collectors.toList());
    }
}

