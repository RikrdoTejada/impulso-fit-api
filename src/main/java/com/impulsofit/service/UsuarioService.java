package com.impulsofit.service;

import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.BusinessRuleException;
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

    //Metodo update para credenciales de usuario (password and email)
    @Transactional
    public UsuarioResponseDTO updateCred(Long id, RecoverRequestDTO req) {
        //Validar usuario por email
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con id " + id));

        //Validar respuesta secreta de usuario
        String respIngresada = req.respuesta();
        if (respIngresada == null ||
                !respIngresada.trim().equalsIgnoreCase(usuario.getRespuesta().trim())) {
            throw new BusinessRuleException("La respuesta es incorrecta.");
        }
        boolean huboCambios = false;

        //Actualizar email
        String newEmail = req.new_email();
        if (newEmail != null && !newEmail.isBlank()) {
            String emailNormalizado = newEmail.trim().toLowerCase();
            // Evitar duplicados
            if (usuarioRepository.existsByEmailIgnoreCase(req.new_email())) {
                throw new AlreadyExistsException("Ya existe un usuario con ese correo.");
            }
            usuario.setEmail(emailNormalizado);
            huboCambios = true;
        }

        //Actualizar contraseña
        String newPass = req.new_contrasena();
        if (newPass != null && !newPass.isBlank()) {
            usuario.setContrasena(newPass);
            huboCambios = true;
        }

        //Regla de negocio: Debe mandar un campo a actualizar
        if (!huboCambios) {
            throw new BusinessRuleException("Debes enviar al menos un campo a actualizar (email y/o contraseña).");
        }

        // Guardar una sola vez
        Usuario saved = usuarioRepository.save(usuario);return mapToResponse(saved);
    }


    private UsuarioResponseDTO mapToResponse(Usuario saved) {
        return new UsuarioResponseDTO(
                saved.getIdUsuario(),
                saved.getEmail(),
                saved.getFechaRegistro(),
                saved.getCodPregunta()
        );
    }

}
