package com.impulsofit.service;

import com.impulsofit.dto.request.PerfilRequestDTO;
import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Perfil;
import com.impulsofit.model.Persona;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final PersonaRepository personaRepository;

    @Transactional
    public PerfilResponseDTO crearPerfil(PerfilRequestDTO req) {
        if(perfilRepository.existsByNombrePerfilIgnoreCase((req.nombre_perfil()))) {
            throw new AlreadyExistsException("Ya existe un perfil con el nombre: " + req.nombre_perfil());
        }
        Persona persona = personaRepository.findById(req.id_persona())
                .orElseThrow(() -> new BusinessRuleException("No existe un persona con el id: " + req.id_persona()));

        Perfil perfilEntity = new Perfil();
        perfilEntity.setNombrePerfil(req.nombre_perfil());
        perfilEntity.setBiografia(req.biografia());
        perfilEntity.setPersona(persona);
        perfilEntity.setUbicacion(req.ubicacion());
        perfilEntity.setFotoPerfil(req.foto_perfil());
        perfilRepository.save(perfilEntity);

        return mapToResponse(perfilEntity);
    }

    @Transactional
    public PerfilResponseDTO actualizarPerfil(Long idPerfil, PerfilRequestDTO request) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new BusinessRuleException("Perfil no encontrado"));

        if (request.nombre_perfil() == null || request.nombre_perfil().trim().isEmpty()) {
            throw new BusinessRuleException("El nombre de usuario no puede estar vacío.");
        }

        // Actualizar campos
        perfil.setBiografia(request.biografia());
        perfil.setUbicacion(request.ubicacion());
        perfil.setFotoPerfil(request.foto_perfil());

        perfilRepository.save(perfil);

        // Convertir a Response usando constructor inmutable
        return mapToResponse(perfil);
    }

    private PerfilResponseDTO mapToResponse(Perfil perfil) {
        return new PerfilResponseDTO(
                perfil.getIdPerfil(),
                perfil.getPersona().getNombres(),
                perfil.getPersona().getGenero()
        );
    }
}
