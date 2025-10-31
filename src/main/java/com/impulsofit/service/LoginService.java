package com.impulsofit.service;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class LoginService {

    private static final int MAX_INTENTOS = 5;
    private static final long HORAS_BLOQUEO = 6;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Mapa en memoria: usuarioId -> fechaHoraBloqueo
    private final ConcurrentMap<Long, LocalDateTime> bloqueos = new ConcurrentHashMap<>();

    @Transactional(noRollbackFor = BusinessRuleException.class)
    public LoginResponseDTO login(LoginRequestDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new BusinessRuleException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Long idUsuario = usuario.getIdUsuario();

        // Si está bloqueado en la tabla, comprobar el mapa en memoria
        if (usuario.getBloqueado() != null && usuario.getBloqueado()) {
            LocalDateTime fechaBloqueo = bloqueos.get(idUsuario);
            if (fechaBloqueo != null) {
                LocalDateTime ahora = LocalDateTime.now();
                LocalDateTime desbloqueo = fechaBloqueo.plusHours(HORAS_BLOQUEO);
                if (ahora.isAfter(desbloqueo)) {
                    // desbloquear automáticamente
                    usuario.setBloqueado(false);
                    usuario.setIntentosFallidos(0);
                    usuarioRepository.save(usuario);
                    bloqueos.remove(idUsuario);
                } else {
                    throw new BusinessRuleException("Usuario bloqueado por múltiples intentos fallidos", HttpStatus.FORBIDDEN);
                }
            } else {
                // Considerar bloqueado por seguridad
                throw new BusinessRuleException("Usuario bloqueado por múltiples intentos fallidos", HttpStatus.FORBIDDEN);
            }
        }

        // Validar contraseña
        if (!usuario.getContrasena().equals(loginDTO.getContrasena())) {
            int intentos = usuario.getIntentosFallidos() == null ? 1 : usuario.getIntentosFallidos() + 1;
            usuario.setIntentosFallidos(intentos);

            boolean bloquear = false;
            if (intentos > MAX_INTENTOS) {
                // En el sexto intento se bloquea
                usuario.setBloqueado(true);
                bloqueos.put(idUsuario, LocalDateTime.now());
                bloquear = true;
            }

            persistirIntentosYBloqueo(idUsuario, intentos, bloquear);

            throw new BusinessRuleException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }

        // Login exitoso: resetear intentos fallidos
        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(false);
        usuarioRepository.save(usuario);

        return new LoginResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
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
        usuarioRepository.save(u);
    }
}
