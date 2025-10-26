package com.impulsofit.service;

import com.impulsofit.dto.request.PublicacionRequest;
import com.impulsofit.dto.response.PublicacionResponse;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.PublicacionType;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PublicacionService {
    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;

    @Transactional
    public PublicacionResponse create(PublicacionRequest publicacion) {
        //Usuario
        Usuario usuario = usuarioRepository.findById(publicacion.id_usuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

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
        publicacionEntity.setUsuario(usuario);

        //Grupo y Tipo
        if (publicacion.id_grupo() == null) {
            publicacionEntity.setGrupo(null);
            publicacionEntity.setType(PublicacionType.GENERAL);
        } else {
            Grupo grupo = grupoRepository.findById(publicacion.id_grupo())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
            //Membresia de Grupo
            boolean member = membresiaGrupoRepository.
                    existsByUsuario_IdUsuarioAndGrupo_IdGrupo(publicacion.id_usuario(), publicacion.id_grupo());
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
    public List<PublicacionResponse> findAll() {
        return publicacionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PublicacionResponse update(Long id, PublicacionRequest publicacion) {
        Publicacion publicacionEntity = publicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la publicacion con id " + id));
        publicacionEntity.setIdPublicacion(id);

        //Usuario
        Usuario usuario = usuarioRepository.findById(publicacion.id_usuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        publicacionEntity.setUsuario(usuario);

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
                    existsByUsuario_IdUsuarioAndGrupo_IdGrupo(publicacion.id_usuario(), publicacion.id_grupo());
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

    private PublicacionResponse mapToResponse(Publicacion publicacion) {
        boolean publica = (publicacion.getType() == PublicacionType.GENERAL);
        String grupoNombre = publica ? null : publicacion.getGrupo().getNombre();
        return new PublicacionResponse(
                publicacion.getIdPublicacion(),
                publicacion.getUsuario().getNombre(),
                publicacion.getType(),
                grupoNombre,
                publicacion.getContenido(),
                publicacion.getFechaPublicacion()
        );
    }
}