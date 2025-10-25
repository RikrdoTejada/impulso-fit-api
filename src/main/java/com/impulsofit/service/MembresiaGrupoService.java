package com.impulsofit.service;

import com.impulsofit.dto.request.MembresiaGrupoRequest;
import com.impulsofit.dto.response.MembresiaGrupoResponse;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.MembresiaGrupo;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class MembresiaGrupoService {
    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;

    public MembresiaGrupoResponse create(MembresiaGrupoRequest membresia) {
        Usuario usuario = usuarioRepository.findById(membresia.id_usuario())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el id: " + membresia.id_usuario()));

        Grupo grupo = grupoRepository.findById(membresia.id_grupo())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el grupo con el id: " + membresia.id_grupo()));

        MembresiaGrupo membresiaEntity = new MembresiaGrupo();
        membresiaEntity.setUsuario(usuario);
        membresiaEntity.setGrupo(grupo);

        MembresiaGrupo saved = membresiaGrupoRepository.save(membresiaEntity);

        return new MembresiaGrupoResponse(
                saved.getIdMembresia(),
                saved.getUsuario().getNombre(),
                saved.getGrupo().getNombre(),
                saved.getFechaUnion()
        );
    }

    public void delete(Long id) {
        if (!membresiaGrupoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe la membresía con el id: " + id);
        }
        membresiaGrupoRepository.deleteById(id);
    }
}
