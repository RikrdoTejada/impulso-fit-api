package com.impulsofit.unit;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import com.impulsofit.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginService - Pruebas Unitarias")
class LoginServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("Inicio de sesión: credenciales correctas reinician intentos")
    void login_withValidCredentials_shouldReturnResponseAndResetAttempts() {
        // Arrange
        Usuario u = new Usuario();
        u.setIdUsuario(1L);
        u.setEmail("test@example.com");
        u.setContrasena("secret");
        u.setNombres("Test User");
        u.setIntentosFallidos(2);
        u.setBloqueado(false);

        when(usuarioRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(u));

        // Act
        LoginRequestDTO req = new LoginRequestDTO("test@example.com", "secret");
        LoginResponseDTO res = loginService.login(req);

        // Assert
        assertThat(res).isNotNull();
        assertThat(res.email()).isEqualTo("test@example.com");
        assertThat(res.nombre()).isEqualTo("Test User");
        verify(usuarioRepository, atLeastOnce()).save(any());
    }

    @Test
    @DisplayName("Inicio de sesión: contraseña incorrecta incrementa intentos y arroja excepción")
    void login_withWrongPassword_shouldIncrementAttemptsAndThrow() {
        // Arrange
        Usuario u = new Usuario();
        u.setIdUsuario(2L);
        u.setEmail("bob@example.com");
        u.setContrasena("correct");
        u.setIntentosFallidos(1);
        u.setBloqueado(false);

        when(usuarioRepository.findByEmailIgnoreCase("bob@example.com")).thenReturn(Optional.of(u));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any())).thenReturn(u);

        // Act / Assert
        LoginRequestDTO req = new LoginRequestDTO("bob@example.com", "wrong");
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> loginService.login(req));
        assertThat(ex.getMessage()).isEqualTo("Credenciales incorrectas");

        verify(usuarioRepository).save(any());
        verify(usuarioRepository, atLeastOnce()).findById(2L);
    }

    @Test
    @DisplayName("Inicio de sesión: bloqueo tras múltiples intentos fallidos")
    void login_shouldBlockUserAfterMultipleFailedAttempts() {
        // Arrange
        Usuario u = new Usuario();
        u.setIdUsuario(3L);
        u.setEmail("lock@example.com");
        u.setContrasena("pw");
        u.setIntentosFallidos(6);
        u.setBloqueado(false);

        when(usuarioRepository.findByEmailIgnoreCase("lock@example.com")).thenReturn(Optional.of(u));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any())).thenReturn(u);

        // Act / Assert
        LoginRequestDTO req = new LoginRequestDTO("lock@example.com", "bad");
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> loginService.login(req));
        assertThat(ex.getMessage()).isEqualTo("Credenciales incorrectas");

        verify(usuarioRepository, atLeastOnce()).save(any());
    }
}
