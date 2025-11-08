package com.impulsofit.unit;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import com.impulsofit.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
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

    // Usuarios reutilizables
    private Usuario mockUserValid;
    private Usuario mockUserBob;
    private Usuario mockUserLock;

    @BeforeEach
    void setUp() {
        mockUserValid = createMockUser(1L, "test@example.com", "secret", "Test User");
        mockUserBob = createMockUser(2L, "bob@example.com", "correct", "Bob");
        mockUserLock = createMockUser(3L, "lock@example.com", "pw", "LockUser");
    }

    private Usuario createMockUser(Long id, String email, String contrasena, String nombres) {
        Usuario u = new Usuario();
        u.setIdUsuario(id);
        u.setEmail(email);
        u.setContrasena(contrasena);
        u.setNombres(nombres);
        return u;
    }

    @Test
    @DisplayName("Inicio de sesión: credenciales correctas reinician intentos")
    void login_withValidCredentials_shouldReturnResponseAndResetAttempts() {
        // Arrange
        mockUserValid.setIntentosFallidos(2);
        mockUserValid.setBloqueado(false);

        when(usuarioRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(mockUserValid));

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
        mockUserBob.setIntentosFallidos(1);
        mockUserBob.setBloqueado(false);

        when(usuarioRepository.findByEmailIgnoreCase("bob@example.com")).thenReturn(Optional.of(mockUserBob));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(mockUserBob));
        when(usuarioRepository.save(any())).thenReturn(mockUserBob);

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
        mockUserLock.setIntentosFallidos(6);
        mockUserLock.setBloqueado(false);

        when(usuarioRepository.findByEmailIgnoreCase("lock@example.com")).thenReturn(Optional.of(mockUserLock));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(mockUserLock));
        when(usuarioRepository.save(any())).thenReturn(mockUserLock);

        // Act / Assert
        LoginRequestDTO req = new LoginRequestDTO("lock@example.com", "bad");
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> loginService.login(req));
        assertThat(ex.getMessage()).isEqualTo("Credenciales incorrectas");

        verify(usuarioRepository, atLeastOnce()).save(any());
    }
}
