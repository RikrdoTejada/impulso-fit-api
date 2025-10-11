package com.impulsofit.service;

import com.impulsofit.dto.request.PublicacionRequest;
import com.impulsofit.dto.response.PublicacionResponse;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor

public class PublicacionService {
    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;

    @Transactional
    public PublicacionResponse create(PublicacionRequest publicacion) {

        Usuario usuario = usuarioRepository.findById(publicacion.id_usuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        //Contenido no puede estar vacio
        if(publicacion.contenido()==null){
            throw new BusinessRuleException("El contenido no puede estar vacio.");
        }
        //Contenido no supera los 500 char
        if(publicacion.contenido().length()>500){
            throw new BusinessRuleException("El contenido no puede ser mayor a 500 caracteres. Longitud actual: " +publicacion.contenido().length());
        }

        Publicacion publicacionEntity = new Publicacion();
        publicacionEntity.setUsuario(usuario);

        //Si la publicacion no pertenece a un grupo, entonces setGrupo(null)
        if (publicacion.id_grupo() ==null) {
            publicacionEntity.setGrupo(null);
        } else {
            Grupo grupo = grupoRepository.findById(publicacion.id_grupo())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
            publicacionEntity.setGrupo(grupo);
        }
        publicacionEntity.setContenido(publicacion.contenido());
        publicacionEntity.setFecha_publicacion(publicacion.fecha_publicacion());

        Publicacion saved = publicacionRepository.save(publicacionEntity);

        boolean publica = (saved.getGrupo() == null);
        String grupoNombre = publica ? null : saved.getGrupo().getNombre();

        return new PublicacionResponse(
                saved.getId_publicacion(),
                saved.getUsuario().getNombre(),
                publica,
                grupoNombre,
                saved.getContenido(),
                saved.getFecha_publicacion()
        );
    }

    @Transactional(readOnly = true)
    public List<Publicacion> findAll() {
        return publicacionRepository.findAll();
    }

    @Transactional
    public Publicacion update(Long id, Publicacion publicacion) {
        if (!publicacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe la publicacion con el id: " + id);
        }
        publicacion.setId_publicacion(id);
        return publicacionRepository.save(publicacion);
    }

    @Transactional
    public void delete(Long id) {
        if (!publicacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe la publicacion con el id: " + id);
        }
        publicacionRepository.deleteById(id);
    }
}
