package com.impulsofit.service;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class LoginService {

    private static final int MAX_INTENTOS = 5;
    private static final long MINUTOS_DESBLOQUEO = 60;

    private final UsuarioRepository usuarioRepository;

    public LoginService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(noRollbackFor = BusinessRuleException.class)
    public LoginResponseDTO login(LoginRequestDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.email())
                .orElseThrow(() -> new BusinessRuleException("Usuario no encontrado"));

        Long idUsuario = usuario.getIdUsuario();

        if (usuario.getBloqueado() != null && usuario.getBloqueado()) {
            LocalDateTime fechaBloqueo = usuario.getFechaBloqueo();
            if (fechaBloqueo != null) {
                LocalDateTime ahora = LocalDateTime.now();
                if (Duration.between(fechaBloqueo, ahora).toMinutes() >= MINUTOS_DESBLOQUEO) {
                    usuario.setBloqueado(false);
                    usuario.setIntentosFallidos(0);
                    usuario.setFechaBloqueo(null);
                    usuarioRepository.save(usuario);
                } else {
                    throw new BusinessRuleException("Usuario bloqueado por múltiples intentos fallidos");
                }
            } else {
                throw new BusinessRuleException("Usuario bloqueado por múltiples intentos fallidos");
            }
        }

        // Validar contraseña
        if (!usuario.getContrasena().equals(loginDTO.contrasena())) {
            int intentos = usuario.getIntentosFallidos() == null ? 1 : usuario.getIntentosFallidos() + 1;
            usuario.setIntentosFallidos(intentos);

            boolean bloquear = false;
            if (intentos > MAX_INTENTOS) {
                // En el sexto intento se bloquea
                usuario.setBloqueado(true);
                usuario.setFechaBloqueo(LocalDateTime.now());
                bloquear = true;
            }

            persistirIntentosYBloqueo(idUsuario, intentos, bloquear);
            throw new BusinessRuleException("Credenciales incorrectas");
        }

        // Login exitoso: resetear intentos fallidos
        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(false);
        usuario.setFechaBloqueo(null);
        usuarioRepository.save(usuario);

        return new LoginResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombres(),
                usuario.getEmail(),
                usuario.getEdad(),
                usuario.getGenero()
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persistirIntentosYBloqueo(Long idUsuario, int intentos, boolean bloquear) {
        Usuario u = usuarioRepository.findById(idUsuario).orElse(null);
        if (u == null) return;
        u.setIntentosFallidos(intentos);
        if (bloquear) u.setBloqueado(true);
        if (bloquear) u.setFechaBloqueo(LocalDateTime.now());
        if (!bloquear) u.setFechaBloqueo(null);
        usuarioRepository.save(u);
    }
}