package com.impulsofit.service;

import com.impulsofit.dto.request.UsuarioRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UsuarioResponseDTO create(UsuarioRequestDTO u) {
        Usuario usuario = new Usuario();
        usuario.setUsername(u.username());
        usuario.setEmail(u.email());
        usuario.setPassword(u.password());
        if (usuario.getCreatedAt() == null) {
            usuario.setCreatedAt(Instant.now());
        }
        Usuario saved = repository.save(usuario);
        return new UsuarioResponseDTO(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getCreatedAt()
        );
    }

    @Override
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO u) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Para PUT: reemplaza todos los campos
        usuario.setUsername(u.username());
        usuario.setEmail(u.email());
        usuario.setPassword(u.password());
        // createdAt se mantiene

        Usuario saved = repository.save(usuario);
        return new UsuarioResponseDTO(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getCreatedAt()
        );
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public UsuarioResponseDTO getById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getCreatedAt()
        );
    }

    @Override
    public List<UsuarioResponseDTO> list() {
        return repository.findAll()
                .stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getUsername(), u.getEmail(), u.getCreatedAt()))
                .toList();
    }
}