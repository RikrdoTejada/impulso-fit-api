package com.impulsofit.service;

import com.impulsofit.dto.request.RespuestaRequest;
import com.impulsofit.dto.response.RespuestaResponse;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Respuesta;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.RespuestaRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RespuestaService {
    private final RespuestaRepository respuestaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public RespuestaResponse create(RespuestaRequest respuesta) {
        Usuario usuario = usuarioRepository.findById(respuesta.id_usuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (respuestaRepository.existsByUsuario_IdUsuario(respuesta.id_usuario())){
            throw new AlreadyExistsException("`Respuesta:` Ya existe en el sistema");
        }

        Respuesta respuestaEntity = new Respuesta();
        respuestaEntity.setUsuario(usuario);
        respuestaEntity.setStrRespuesta(respuesta.str_respuesta());

        Respuesta saved = respuestaRepository.save(respuestaEntity);

        return new RespuestaResponse(
                saved.getIdRespuesta(),
                saved.getUsuario().getNombres(),
                saved.getStrRespuesta()
        );
    }

    @Transactional
    public void delete(Long id) {
        if (!respuestaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe respuesta con el id: " + id);
        }
        respuestaRepository.deleteById(id);
    }
}
