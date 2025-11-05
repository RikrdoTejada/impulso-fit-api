package com.impulsofit.service;

import com.impulsofit.dto.request.CrearPublicacionRequestDTO;
import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.RetoRepository;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class PublicacionService {

    private final PublicacionRepository posts;
    private final UsuarioRepository users;
    private final GrupoRepository groups;
    private final RetoRepository challenges;

    public PublicacionService(PublicacionRepository posts, UsuarioRepository users, GrupoRepository groups, RetoRepository challenges) {
        this.posts = posts;
        this.users = users;
        this.groups = groups;
        this.challenges = challenges;
    }

    @Transactional
    public PublicacionResponseDTO create(CrearPublicacionRequestDTO req) {
        // Para record: req.userId(), req.content(), req.challengeId(), req.groupId()
        Usuario usuario = users.findById(req.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + req.userId()));

        Publicacion publicacion = new Publicacion();
        publicacion.setUser(usuario);
        publicacion.setContent(req.content());
        publicacion.setCreatedAt(Instant.now());

        if (req.challengeId() != null) {
            Reto ch = challenges.findById(req.challengeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Challenge not found with id: " + req.challengeId()));
            publicacion.setChallenge(ch);
            publicacion.setGroup(ch.getGroup()); // satisface NOT NULL en grupo_id
        } else if (req.groupId() != null) {
            Grupo g = groups.findById(req.groupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + req.groupId()));
            publicacion.setGroup(g);
        } else {
            throw new IllegalArgumentException("Post must belong to a group or challenge");
        }

        Publicacion saved = posts.save(publicacion);

        return new PublicacionResponseDTO(
                saved.getId(),
                saved.getUser().getId(),
                saved.getContent(),
                saved.getChallenge() != null ? saved.getChallenge().getId() : null,
                saved.getGroup().getId(),
                saved.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> listAll() {
        return posts.findAll().stream()
                .map(p -> new PublicacionResponseDTO(
                        p.getId(),
                        p.getUser().getId(),
                        p.getContent(),
                        p.getChallenge() != null ? p.getChallenge().getId() : null,
                        p.getGroup().getId(),
                        p.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> listByUser(Long userId) {
        // Validar que el usuario exista (opcional pero coherente)
        users.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return posts.findAllByUserId(userId).stream()
                .map(p -> new PublicacionResponseDTO(
                        p.getId(),
                        p.getUser().getId(),
                        p.getContent(),
                        p.getChallenge() != null ? p.getChallenge().getId() : null,
                        p.getGroup().getId(),
                        p.getCreatedAt()
                ))
                .toList();
    }
}