package com.impulsofit.service;

import com.impulsofit.dto.request.PerfilRequestDTO;
import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Perfil;
import com.impulsofit.repository.PerfilRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public PerfilResponseDTO actualizarPerfil(Long idPerfil, PerfilRequestDTO request) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new BusinessRuleException("Perfil no encontrado", HttpStatus.NOT_FOUND));

        // Business rule: nombre obligatorio
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new BusinessRuleException("El nombre de usuario no puede estar vacío.", HttpStatus.BAD_REQUEST);
        }

        // Actualizar campos
        perfil.setNombre(request.getNombre().trim());
        perfil.setApellido(request.getApellido());
        perfil.setBiografia(request.getBiografia());
        perfil.setUbicacion(request.getUbicacion());
        perfil.setFotoPerfil(request.getFotoPerfil());

        perfilRepository.save(perfil);

        // Convertir a Response usando constructor inmutable
        return new PerfilResponseDTO(
                perfil.getIdPerfil(),
                perfil.getNombre(),
                perfil.getApellido(),
                perfil.getBiografia(),
                perfil.getUbicacion(),
                perfil.getFotoPerfil()
        );
    }
}
