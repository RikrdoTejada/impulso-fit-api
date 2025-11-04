package com.impulsofit.service;

import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.request.UsuarioRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.RecoverType;
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
@RequiredArgsConstructor()
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RespuestaRepository respuestaRepository;

    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO usuario) {
        //Validacion de correo
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
        //Validaciones
        Usuario usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con id " + id));

        validarDateyGender(usuario);

        //Sets excepto credenciales
        usuarioEntity.setIdUsuario(id);
        usuarioEntity.setNombres(usuario.nombres());
        usuarioEntity.setApellidoP(usuario.apellido_p());
        usuarioEntity.setApellidoM(usuario.apellido_m());
        usuarioEntity.setFechaNacimiento(usuario.fecha_nacimiento());
        usuarioEntity.setGenero(usuario.genero());

        Usuario saved = usuarioRepository.save(usuarioEntity);

        return mapToResponse(saved);
    }

    //Metodo update para credenciales de usuario (password and email)
    @Transactional
    public UsuarioResponseDTO updateCred(Long id, RecoverRequestDTO usercred) {
        Usuario usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con id " + id ));
        Respuesta resp = respuestaRepository.findByUsuario_IdUsuario(usuarioEntity.getIdUsuario());

        if(!usercred.respuesta().equals(resp.getStrRespuesta())){
            throw new BusinessRuleException("La respuesta es incorrecta.");
        }

        if (usercred.tipo_recover() == RecoverType.PASSWORD) {
            if (usercred.new_contrasena() == null || usercred.new_contrasena().isBlank())
                throw new IllegalArgumentException("La nueva contraseña está vacía.");
            usuarioEntity.setContrasena(usercred.new_contrasena());
            Usuario saved = usuarioRepository.save(usuarioEntity);
            return mapToResponse(saved);

        } else {
            if (usercred.new_email() == null || usercred.new_email().isBlank())
                throw new IllegalArgumentException("El nuevo email está vacío");
            usuarioEntity.setEmail(usercred.new_email().toLowerCase());
            Usuario saved = usuarioRepository.save(usuarioEntity);
            return mapToResponse(saved);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el usuario con el id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private void validarDateyGender(UsuarioRequestDTO u) {
        //Fecha no puede estar vacía
        if (u.fecha_nacimiento() == null) {
            throw new BusinessRuleException("La fecha de nacimiento no puede estar vacía.");
        }
        //Fecha no puede ser del futuro
        if (u.fecha_nacimiento().isAfter(LocalDate.now())) {
            throw new BusinessRuleException(
                    "La fecha de nacimiento no puede ser una fecha futura. " +
                            "Fecha ingresada: " + u.fecha_nacimiento()
            );
        }
        //Usuario debe tener mínimo 15 años de edad
        int edad = Period.between(u.fecha_nacimiento(), LocalDate.now()).getYears();
        if (edad < 15) {
            throw new BusinessRuleException(
                    "El usuario debe tener al menos 15 años. Edad actual: " + edad
            );
        }
        //Genero no puede estar vacío
        if (u.genero() == null) {
            throw new BusinessRuleException("Genero no puede estar vacío");
        }
        //Usuario no puede ser diferente de "M" o "F"
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