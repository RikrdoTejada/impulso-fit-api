package com.impulsofit.service;

import com.impulsofit.dto.request.GrupoRequest;
import com.impulsofit.dto.response.GrupoResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Deporte;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.DeporteRepository;
import com.impulsofit.repository.UsuarioRepository;
import com.impulsofit.repository.GrupoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DeporteRepository deporteRepository;

    public GrupoResponse create(GrupoRequest grupo){

        Usuario usuario = usuarioRepository.findById(grupo.id_usuario_creador())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario Creardor no encontrado"));

        Deporte deporte = deporteRepository.findById(grupo.id_deporte())
                .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado"));

        Grupo grupoEntity = new Grupo();
        grupoEntity.setCreador(usuario);
        grupoEntity.setDeporte(deporte);
        grupoEntity.setNombre(grupo.nombre());
        grupoEntity.setDescripcion(grupo.descripcion());
        grupoEntity.setUbicacion(grupo.ubicacion());

        Grupo saved = grupoRepository.save(grupoEntity);
        return new GrupoResponse(
                saved.getIdGrupo(),
                saved.getCreador().getNombre(),
                saved.getDeporte().getNombre(),
                saved.getNombre(),
                saved.getDescripcion(),
                saved.getUbicacion(),
                saved.getFechaCreacion()
        );

    }

    public void delete(Long id) {
        if (!grupoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el grupo con el id: " + id);
        }
        grupoRepository.deleteById(id);
    }
}