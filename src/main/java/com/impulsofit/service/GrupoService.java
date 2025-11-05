package com.impulsofit.service;

import com.impulsofit.dto.request.CrearGrupoRequestDTO;
import com.impulsofit.dto.response.GrupoResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class GrupoService {

    private final GrupoRepository groups;
    private final UsuarioRepository users;

    public GrupoService(GrupoRepository groups, UsuarioRepository users) {
        this.groups = groups;
        this.users = users;
    }

    @Transactional
    public GrupoResponseDTO create(CrearGrupoRequestDTO req) {
        Usuario creator = users.findById(req.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + req.getCreatedById()));

        Grupo g = new Grupo();
        g.setName(req.getName());
        g.setDescription(req.getDescription());
        g.setCreatedBy(creator);
        g.setCreatedAt(Instant.now());

        Grupo saved = groups.save(g);

        return new GrupoResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getCreatedBy().getId(),
                saved.getCreatedBy().getUsername(),
                saved.getCreatedAt()
        );
    }
}

