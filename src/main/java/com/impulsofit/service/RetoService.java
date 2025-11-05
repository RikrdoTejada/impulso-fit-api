package com.impulsofit.service;

import com.impulsofit.dto.request.CrearRetoRequestDTO;
import com.impulsofit.dto.response.RetoResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.RetoRepository;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RetoService {

    private final RetoRepository retoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;

    public RetoService(RetoRepository retoRepository,
                       UsuarioRepository usuarioRepository,
                       GrupoRepository grupoRepository) {
        this.retoRepository = retoRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
    }

    @Transactional
    public RetoResponseDTO create(CrearRetoRequestDTO req) {
        if (req.groupId() == null) {
            throw new IllegalArgumentException("groupId is required");
        }
        if (req.createdById() == null) {
            throw new IllegalArgumentException("createdById is required");
        }

        Usuario createdBy = usuarioRepository.findById(req.createdById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + req.createdById()));

        Grupo grupo = grupoRepository.findById(req.groupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + req.groupId()));

        Reto ch = new Reto();
        ch.setTitle(req.title());
        ch.setDescription(req.description());
        ch.setStartDate(req.startDate());
        ch.setEndDate(req.endDate());
        ch.setCreatedAt(Instant.now());
        ch.setCreatedBy(createdBy);
        ch.setGroup(grupo);

        Reto saved = retoRepository.save(ch);

        return new RetoResponseDTO(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStartDate(),
                saved.getEndDate(),
                saved.getCreatedAt(),
                saved.getCreatedBy().getId(),
                saved.getGroup().getId()
        );
    }
}