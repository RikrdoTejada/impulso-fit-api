package com.impulsofit.service;

import com.impulsofit.dto.request.UnidadRequest;
import com.impulsofit.dto.response.UnidadResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Unidad;
import com.impulsofit.repository.UnidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UnidadService {
    private final UnidadRepository unidadRepository;

    public UnidadResponse create(UnidadRequest unidad) {

        Unidad unidadEntity = new Unidad();
        unidadEntity.setNombre(unidad.nombre());
        unidadEntity.setUso(unidad.uso());

        Unidad saved = unidadRepository.save(unidadEntity);

        return new UnidadResponse(
                saved.getIdUnidad(),
                saved.getNombre(),
                saved.getUso()
        );
    }

    public void delete(Long id) {
        if (!unidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe la unidad con el id: " + id);
        }
        unidadRepository.deleteById(id);
    }
}
