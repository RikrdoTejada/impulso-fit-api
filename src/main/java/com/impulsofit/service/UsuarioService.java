package com.impulsofit.service;

import com.impulsofit.dto.request.UsuarioRequest;
import com.impulsofit.dto.response.UsuarioResponse;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor()
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponse create(UsuarioRequest usuario) {

        if (usuarioRepository.existsByEmailIgnoreCase(usuario.email())) {
            throw new AlreadyExistsException("Ya existe un usuario con el correo: " + usuario.email());
        }

        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setNombre(usuario.nombre());
        usuarioEntity.setEmail(usuario.email());
        usuarioEntity.setContrasena(usuario.contrasena());
        usuarioEntity.setEdad(usuario.edad());
        usuarioEntity.setGenero(usuario.genero());
        usuarioEntity.setFechaRegistro(usuario.fecha_registro());

        Usuario saved = usuarioRepository.save(usuarioEntity);

        return new UsuarioResponse(
                saved.getIdUsuario(),
                saved.getNombre(),
                saved.getEmail(),
                saved.getContrasena(),
                saved.getEdad(),
                saved.getGenero(),
                saved.getFechaRegistro()
        );
    }
    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el usuario con el id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

}