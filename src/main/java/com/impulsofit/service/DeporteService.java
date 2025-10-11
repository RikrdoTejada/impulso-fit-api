package com.impulsofit.service;

import com.impulsofit.dto.request.DeporteRequest;
import com.impulsofit.dto.response.DeporteResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Deporte;
import com.impulsofit.repository.DeporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeporteService {

    private final DeporteRepository deporteRepository;

    public DeporteResponse create(DeporteRequest deporte) {

        Deporte deporteEntity = new Deporte();
        deporteEntity.setNombre(deporte.nombre());
        deporteEntity.setTipo(deporte.tipo());

        Deporte saved = deporteRepository.save(deporteEntity);

        return new DeporteResponse(
                saved.getId(),
                saved.getNombre(),
                saved.getNombre()
        );
    }

    public void delete(Long id) {
        if (!deporteRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el deporte con el id: " + id);
        }
        deporteRepository.deleteById(id);
    }
}