package com.impulsofit.service;

import com.impulsofit.dto.request.*;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Persona;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.PersonaRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final int MAX_INTENTOS = 5;
    private static final long MINUTOS_DESBLOQUEO = 60;

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;


    @Transactional
    public UsuarioResponseDTO register(RegisterRequestDTO req) {
        //Validacion de correo
        if (usuarioRepository.existsByEmailIgnoreCase(req.email())) {
            throw new AlreadyExistsException("Ya existe un usuario con el correo: " + req.email());
        }

        validarDateyGender(req);

        //Crear Usuario con credenciales
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setEmail(req.email().toLowerCase());
        usuarioEntity.setContrasena(req.contrasena());
        usuarioEntity.setCodPregunta(req.cod_pregunta());
        usuarioEntity.setRespuesta(req.respuesta());

        Usuario saved = usuarioRepository.save(usuarioEntity);

        //Crear Persona asociada al Usuario
        Persona personaEntity = new Persona();
        personaEntity.setNombres(req.nombres());
        personaEntity.setApellidoP(req.apellido_p());
        personaEntity.setApellidoM(req.apellido_m());
        personaEntity.setFechaNacimiento(req.fecha_nacimiento());
        personaEntity.setGenero(req.genero());

        return mapToResponse(saved);
    }

    @Transactional(noRollbackFor = BusinessRuleException.class)
    public LoginResponseDTO login(LoginRequestDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(loginDTO.email())
                .orElseThrow(() -> new BusinessRuleException("Usuario no encontrado"));

        Persona persona = personaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessRuleException("Persona no encontrada"));

        if (persona.getBloqueado() != null && persona.getBloqueado()) {
            LocalDateTime fechaBloqueo = persona.getFechaBloqueo();
            if (fechaBloqueo != null) {
                LocalDateTime ahora = LocalDateTime.now();
                if (Duration.between(fechaBloqueo, ahora).toMinutes() >= MINUTOS_DESBLOQUEO) {
                    persona.setBloqueado(false);
                    persona.setIntentosFallidos(0);
                    persona.setFechaBloqueo(null);
                    personaRepository.save(persona);
                } else {
                    throw new BusinessRuleException("Usuario bloqueado por múltiples intentos fallidos");
                }
            } else {
                throw new BusinessRuleException("Usuario bloqueado por múltiples intentos fallidos");
            }
        }

        // Validar contraseña
        if (!usuario.getContrasena().equals(loginDTO.contrasena())) {
            int intentos = persona.getIntentosFallidos() == null ? 1 : persona.getIntentosFallidos() + 1;
            persona.setIntentosFallidos(intentos);

            boolean bloquear = false;
            if (intentos > MAX_INTENTOS) {
                // En el sexto intento se bloquea
                persona.setBloqueado(true);
                persona.setFechaBloqueo(LocalDateTime.now());
                bloquear = true;
            }

            persistirIntentosYBloqueo(persona.getIdPersona(), intentos, bloquear);
            throw new BusinessRuleException("Credenciales incorrectas");
        }

        // Login exitoso: resetear intentos fallidos
        persona.setIntentosFallidos(0);
        persona.setBloqueado(false);
        persona.setFechaBloqueo(null);
        usuarioRepository.save(usuario);

        return new LoginResponseDTO(
                usuario.getEmail(),
                persona.getNombres()
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persistirIntentosYBloqueo(Long idpersona, int intentos, boolean bloquear) {
        Persona u = personaRepository.findById(idpersona).orElse(null);
        if (u == null) return;
        u.setIntentosFallidos(intentos);
        if (bloquear) u.setBloqueado(true);
        if (bloquear) u.setFechaBloqueo(LocalDateTime.now());
        if (!bloquear) u.setFechaBloqueo(null);
        personaRepository.save(u);
    }

    @Transactional
    public UsuarioResponseDTO recoverCred(RecoverRequestDTO req) {
        Usuario usuarioEntity = usuarioRepository.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario registrado con el email:  "
                        + req.email() ));
        String respIngresada = req.respuesta();
        if (respIngresada == null ||
                !respIngresada.trim().equalsIgnoreCase(usuarioEntity.getRespuesta().trim())) {
            throw new BusinessRuleException("La respuesta es incorrecta.");
        }
        String newPass = req.new_contrasena();
        if (newPass != null && !newPass.isBlank()) {
            usuarioEntity.setContrasena(newPass);
        }
        Usuario saved = usuarioRepository.save(usuarioEntity);

        return mapToResponse(saved);
    }

    private void validarDateyGender(RegisterRequestDTO u) {
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
                saved.getEmail(),
                saved.getFechaRegistro(),
                saved.getCodPregunta()
        );
    }
}