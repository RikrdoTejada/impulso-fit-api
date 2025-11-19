package com.impulsofit.service;

import com.impulsofit.dto.request.DeporteRequestDTO;
import com.impulsofit.dto.response.DeporteResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
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
                .map(d -> new DeporteResponseDTO(
                        d.getIdDeporte(),
                        d.getNombre(),
                        d.getTipo()
                ))
                .collect(Collectors.toList());
    }

    public DeporteResponseDTO create(DeporteRequestDTO deporte) {
        Deporte deporteEntity = new Deporte();
        deporteEntity.setNombre(deporte.nombre());

        if (deporte.tipo_deporte() != null) {
            deporteEntity.setTipoDeporte(deporte.tipo_deporte());
        }

        Deporte saved = deporteRepository.save(deporteEntity);

        return new DeporteResponseDTO(
                saved.getIdDeporte(),
                saved.getNombre(),
                saved.getTipo()
        );
    }

    public void delete(Long id) {
        if (!deporteRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el deporte con el id: " + id);
        }
        deporteRepository.deleteById(id);
    }
}