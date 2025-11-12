package com.impulsofit.service;

import com.impulsofit.dto.request.PublicacionRequestDTO;
import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.*;
import com.impulsofit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PublicacionService {
    private final PublicacionRepository publicacionRepository;
    private final GrupoRepository grupoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final PerfilRepository perfilRepository;

    @Transactional
    public PublicacionResponseDTO create(PublicacionRequestDTO publicacion) {
        //Perfil
        Perfil perfil = perfilRepository.findById(publicacion.id_perfil())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));

        //Contenido no puede estar vacio
        if(publicacion.contenido()==null){
            throw new BusinessRuleException("El contenido no puede estar vacio.");
        }
        //Contenido no supera los 500 char
        if(publicacion.contenido().length()>500){
            throw new BusinessRuleException("El contenido no puede ser mayor a 500 caracteres." +
                    " Longitud actual: " +publicacion.contenido().length());
        }

        Publicacion publicacionEntity = new Publicacion();
        publicacionEntity.setPerfil(perfil);

        //Grupo y Tipo
        if (publicacion.id_grupo() == null) {
            publicacionEntity.setGrupo(null);
            publicacionEntity.setType(PublicacionType.GENERAL);
        } else {
            Grupo grupo = grupoRepository.findById(publicacion.id_grupo())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
            //Membresia de Grupo
            boolean member = membresiaGrupoRepository.
                    existsByPerfil_IdPerfilAndGrupo_IdGrupo(publicacion.id_perfil(), publicacion.id_grupo());
            if (!member) throw new BusinessRuleException("El usuario no pertenece al grupo");
            publicacionEntity.setGrupo(grupo);
            publicacionEntity.setType(PublicacionType.GROUP);
        }

        //Contenido
        publicacionEntity.setContenido(publicacion.contenido());

        Publicacion saved = publicacionRepository.save(publicacionEntity);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> findAll() {
        return publicacionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> findByUsuario_IdUsuario(Long id_perfil) {
        return publicacionRepository.findAllByPerfil_IdPerfil(id_perfil)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> findByGrupo_IdGrupo(Long id_grupo) {
        return publicacionRepository.findAllByGrupo_IdGrupo(id_grupo)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PublicacionResponseDTO update(Long id, PublicacionRequestDTO publicacion) {
        Publicacion publicacionEntity = publicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la publicacion con id " + id));
        publicacionEntity.setIdPublicacion(id);

        //Perfil
        Perfil perfil = perfilRepository.findById(publicacion.id_perfil())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));
        publicacionEntity.setPerfil(perfil);

        //Contenido no puede estar vacio
        if(publicacion.contenido()==null || publicacion.contenido().isBlank()){
            throw new BusinessRuleException("El contenido no puede estar vacio.");
        }

        //Contenido no supera los 500 char
        if(publicacion.contenido().length()>500){
            throw new BusinessRuleException("El contenido no puede ser mayor a 500 caracteres. " +
                    "Longitud actual: " +publicacion.contenido().length());
        }

        //Grupo y Tipo
        if (publicacion.id_grupo() == null) {
            publicacionEntity.setGrupo(null);
            publicacionEntity.setType(PublicacionType.GENERAL);
        } else {
            Grupo grupo = grupoRepository.findById(publicacion.id_grupo())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
            //Membresia de Grupo
            boolean member = membresiaGrupoRepository.
                    existsByPerfil_IdPerfilAndGrupo_IdGrupo(publicacion.id_perfil(), publicacion.id_grupo());
            if (!member) throw new BusinessRuleException("El usuario no pertenece al grupo");
            publicacionEntity.setGrupo(grupo);
            publicacionEntity.setType(PublicacionType.GROUP);
        }

        //Contenido
        publicacionEntity.setContenido(publicacion.contenido());

        Publicacion saved = publicacionRepository.save(publicacionEntity);

        return mapToResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!publicacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe la publicacion con el id: " + id);
        }
        publicacionRepository.deleteById(id);
    }

    private PublicacionResponseDTO mapToResponse(Publicacion publicacion) {
        boolean publica = (publicacion.getType() == PublicacionType.GENERAL);
        String grupoNombre = publica ? null : publicacion.getGrupo().getNombre();
        return new PublicacionResponseDTO(
                publicacion.getIdPublicacion(),
                publicacion.getPerfil().getPersona().getNombres(),
                publicacion.getType(),
                grupoNombre,
                publicacion.getContenido(),
                publicacion.getFechaPublicacion()
        );
    }
}