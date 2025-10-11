package com.impulsofit.service;

import com.impulsofit.dto.request.UsuarioRequest;
import com.impulsofit.dto.response.UsuarioResponse;
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

    public UsuarioResponse create(UsuarioRequest usuario) {

       Usuario usuarioEntity = new Usuario();
       usuarioEntity.setNombre(usuario.nombre());
       usuarioEntity.setEmail(usuario.email());
       usuarioEntity.setContrasena(usuario.contrasena());
       usuarioEntity.setEdad(usuario.edad());
       usuarioEntity.setGenero(usuario.genero());
       usuarioEntity.setFecha_registro(usuario.fecha_registro());

       Usuario saved = usuarioRepository.save(usuarioEntity);

       return new UsuarioResponse(
               saved.getId_usuario(),
               saved.getNombre(),
               saved.getEmail(),
               saved.getContrasena(),
               saved.getEdad(),
               saved.getGenero(),
               saved.getFecha_registro()
       );
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el usuario con el id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

}
