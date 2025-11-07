package com.impulsofit.service;

import com.impulsofit.dto.response.SeguidorResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Seguidor;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.SeguidorRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeguidorService {

    private final SeguidorRepository seguidorRepository;
    private final UsuarioRepository usuarioRepository;

    public SeguidorService(SeguidorRepository seguidorRepository, UsuarioRepository usuarioRepository) {
        this.seguidorRepository = seguidorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public SeguidorResponseDTO follow(Long followerId, Long targetId) {
        if (followerId.equals(targetId)) {
            throw new IllegalArgumentException("No puedes seguirte a ti mismo.");
        }

        Usuario follower = usuarioRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("User (follower) not found: " + followerId));

        Usuario following = usuarioRepository.findById(targetId)
                .orElseThrow(() -> new ResourceNotFoundException("User (target) not found: " + targetId));

        if (seguidorRepository.existsByFollower_IdAndFollowing_Id(followerId, targetId)) {
            // ya existe, devolvemos la relación actual
            Seguidor existing = seguidorRepository.findByFollower_IdAndFollowing_Id(followerId, targetId).get();
            return toResponse(existing);
        }

        Seguidor saved = seguidorRepository.save(new Seguidor(follower, following));
        return toResponse(saved);
    }

    @Transactional
    public void unfollow(Long followerId, Long targetId) {
        Seguidor f = seguidorRepository.findByFollower_IdAndFollowing_Id(followerId, targetId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Follow relation not found (follower=" + followerId + ", following=" + targetId + ")"));

        seguidorRepository.delete(f);
    }

    @Transactional(readOnly = true)
    public List<SeguidorResponseDTO> followersOf(Long userId) {
        // quiénes siguen a userId
        return seguidorRepository.findByFollowing_Id(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SeguidorResponseDTO> followingOf(Long userId) {
        // a quién sigue userId
        return seguidorRepository.findByFollower_Id(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private SeguidorResponseDTO toResponse(Seguidor f) {
        return new SeguidorResponseDTO(
                f.getId(),
                f.getFollower().getId(),
                f.getFollowing().getId(),
                f.getCreatedAt()
        );
    }
}