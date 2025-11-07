package com.impulsofit.service;

import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Respuesta;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.RespuestaRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RecoverService {

    private final UsuarioRepository  usuarioRepository;
    private final RespuestaRepository respuestaRepository;

    public RecoverService(UsuarioRepository usuarioRepository, RespuestaRepository respuestaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.respuestaRepository = respuestaRepository;
    }

    @Transactional
    public UsuarioResponseDTO recoverCred(RecoverRequestDTO req) {
        Usuario usuarioEntity = usuarioRepository.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario registrado con el email:  "
                        + req.email() ));
        Respuesta r = respuestaRepository.findByUsuario_IdUsuario(usuarioEntity.getId());
        if (r == null) {
            throw new ResourceNotFoundException("El usuario no tiene respuesta secreta registrada.");
        }
        String respIngresada = req.respuesta();
        if (respIngresada == null ||
                !respIngresada.trim().equalsIgnoreCase(r.getStrRespuesta().trim())) {
            throw new BusinessRuleException("La respuesta es incorrecta.");
        }
        String newPass = req.new_contrasena();
        if (newPass != null && !newPass.isBlank()) {
            usuarioEntity.setContrasena(newPass);
        }
        Usuario saved = usuarioRepository.save(usuarioEntity);

        return new UsuarioResponseDTO(
                saved.getIdUsuario(),
                saved.getNombres(),
                saved.getApellidoP(),
                saved.getApellidoM(),
                saved.getEmail(),
                saved.getContrasena(),
                saved.getFechaNacimiento(),
                saved.getGenero(),
                saved.getFechaRegistro(),
                saved.getCodPregunta()
        );
    }
}
