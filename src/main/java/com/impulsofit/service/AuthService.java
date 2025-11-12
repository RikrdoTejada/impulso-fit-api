package com.impulsofit.service;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Persona;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.repository.PersonaRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final int MAX_INTENTOS = 5;
    private static final long MINUTOS_DESBLOQUEO = 60;

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PersonaRepository personaRepository;


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

        return new UsuarioResponseDTO(
                saved.getIdUsuario(),
                saved.getEmail(),
                saved.getContrasena(),
                saved.getFechaRegistro(),
                saved.getCodPregunta()
        );
    }
}