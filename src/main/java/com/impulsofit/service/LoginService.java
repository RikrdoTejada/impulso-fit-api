package com.impulsofit.service;

import com.impulsofit.dto.request.LoginRequest;
import com.impulsofit.dto.response.LoginResponse;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    private static final int MAX_INTENTOS = 5;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public LoginResponse login(LoginRequest loginDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new BusinessRuleException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        if (usuario.isBloqueado()) {
            throw new BusinessRuleException("Usuario bloqueado por múltiples intentos fallidos", HttpStatus.FORBIDDEN);
        }

        if (!usuario.getContrasena().equals(loginDTO.getContrasena())) {
            int intentos = usuario.getIntentosFallidos() + 1;
            usuario.setIntentosFallidos(intentos);
            if (intentos >= MAX_INTENTOS) {
                usuario.setBloqueado(true);
            }
            usuarioRepository.save(usuario);
            throw new BusinessRuleException("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }

        // Login exitoso: resetear intentos fallidos
        usuario.setIntentosFallidos(0);
        usuarioRepository.save(usuario);

        return new LoginResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getEdad(),
                usuario.getGenero()
        );
    }
}
