package com.impulsofit.service;

import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.request.UsuarioRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Respuesta;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.RespuestaRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RespuestaRepository respuestaRepository;

    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO usuario) {
        if (usuarioRepository.existsByEmailIgnoreCase(usuario.email())) {
            throw new AlreadyExistsException("Ya existe un usuario con el correo: " + usuario.email());
        }

        validarDateyGender(usuario);
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setNombres(usuario.nombres());
        usuarioEntity.setApellidoP(usuario.apellido_p());
        usuarioEntity.setApellidoM(usuario.apellido_m());
        usuarioEntity.setEmail(usuario.email().toLowerCase());
        usuarioEntity.setContrasena(usuario.contrasena());
        usuarioEntity.setFechaNacimiento(usuario.fecha_nacimiento());
        usuarioEntity.setGenero(usuario.genero());
        usuarioEntity.setCodPregunta(usuario.cod_pregunta());
        Usuario saved = usuarioRepository.save(usuarioEntity);
        return mapToResponse(saved);
    }

    @Transactional
    public UsuarioResponseDTO updateInfo(Long id, UsuarioRequestDTO usuario) {
        Usuario usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con id " + id));
        validarDateyGender(usuario);

        usuarioEntity.setIdUsuario(id);
        usuarioEntity.setNombres(usuario.nombres());
        usuarioEntity.setApellidoP(usuario.apellido_p());
        usuarioEntity.setApellidoM(usuario.apellido_m());
        usuarioEntity.setFechaNacimiento(usuario.fecha_nacimiento());
        usuarioEntity.setGenero(usuario.genero());
        Usuario saved = usuarioRepository.save(usuarioEntity);
        return mapToResponse(saved);
    }

    @Transactional
    public UsuarioResponseDTO updateCred(Long id, RecoverRequestDTO req) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con id " + id));

        Respuesta r = respuestaRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
        if (r == null) {
            throw new ResourceNotFoundException("El usuario no tiene respuesta secreta registrada.");
        }

        String respIngresada = req.respuesta();
        if (respIngresada == null || !respIngresada.trim().equalsIgnoreCase(r.getStrRespuesta().trim())) {
            throw new BusinessRuleException("La respuesta es incorrecta.");
        }

        boolean huboCambios = false;
        String newEmail = req.new_email();
        if (newEmail != null && !newEmail.isBlank()) {
            String emailNormalizado = newEmail.trim().toLowerCase();
            if (usuarioRepository.existsByEmailIgnoreCase(req.new_email())) {
                throw new AlreadyExistsException("Ya existe un usuario con ese correo.");
            }
            usuario.setEmail(emailNormalizado);
            huboCambios = true;
        }

        String newPass = req.new_contrasena();
        if (newPass != null && !newPass.isBlank()) {
            usuario.setContrasena(newPass);
            huboCambios = true;
        }

        if (!huboCambios) {
            throw new BusinessRuleException("Debes enviar al menos un campo a actualizar (email y/o contraseña).");
        }

        Usuario saved = usuarioRepository.save(usuario);
        return mapToResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el usuario con el id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private void validarDateyGender(UsuarioRequestDTO u) {
        if (u.fecha_nacimiento() == null) {
            throw new BusinessRuleException("La fecha de nacimiento no puede estar vacía.");
        }
        if (u.fecha_nacimiento().isAfter(LocalDate.now())) {
            throw new BusinessRuleException("La fecha de nacimiento no puede ser una fecha futura.");
        }

        int edad = Period.between(u.fecha_nacimiento(), LocalDate.now()).getYears();
        if (edad < 15) {
            throw new BusinessRuleException("El usuario debe tener al menos 15 años. Edad actual: " + edad);
        }

        if (u.genero() == null) {
            throw new BusinessRuleException("Genero no puede estar vacío");
        }
        if (!u.genero().equals("M") && !u.genero().equals("F")) {
            throw new BusinessRuleException("Formato de género inválido. Solo se permite: M o F");
        }
    }

    private UsuarioResponseDTO mapToResponse(Usuario saved) {
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

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}