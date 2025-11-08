package com.impulsofit.unit;

import com.impulsofit.model.Publicacion;
import com.impulsofit.model.PublicacionType;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.repository.UsuarioRepository;
import com.impulsofit.service.ComentarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ComentarioService - Pruebas Unitarias")
class ComentarioServiceTest {

    @Mock
    private PublicacionRepository publicacionRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private MembresiaGrupoRepository membresiaRepo;

    @InjectMocks
    private ComentarioService comentarioService;

    // Datos reutilizables para los tests
    private Usuario mockUser1;
    private Usuario mockUser2;
    private Usuario mockUser3;

    private Publicacion pubGeneral1;
    private Publicacion pubGeneral2;
    private Publicacion pubGrupal;

    @BeforeEach
    void setUp() {
        // Inicializar usuarios y publicaciones comunes
        mockUser1 = createMockUser(1L, "u@example.com", "User");
        mockUser2 = createMockUser(2L, "u2@example.com", "User2");
        mockUser3 = createMockUser(3L, "u3@example.com", "User3");

        pubGeneral1 = createMockPublicacion(1L, PublicacionType.GENERAL, null, "content1");
        pubGeneral2 = createMockPublicacion(2L, PublicacionType.GENERAL, null, "content2");
        pubGrupal = createMockPublicacion(3L, PublicacionType.GROUP, createMockGrupo(7L), "Hola grupo");
    }

    // Helper methods solicitados por test.md
    private Usuario createMockUser(Long id, String email, String nombres) {
        Usuario u = new Usuario();
        u.setIdUsuario(id);
        u.setEmail(email);
        u.setNombres(nombres);
        return u;
    }

    private Publicacion createMockPublicacion(Long id, PublicacionType type, com.impulsofit.model.Grupo grupo, String contenido) {
        Publicacion p = new Publicacion();
        p.setIdPublicacion(id);
        p.setType(type);
        p.setGrupo(grupo);
        p.setContenido(contenido);
        return p;
    }

    private com.impulsofit.model.Grupo createMockGrupo(Long id) {
        com.impulsofit.model.Grupo g = new com.impulsofit.model.Grupo(); g.setIdGrupo(id); return g;
    }

    @Test
    @DisplayName("Comentar: no permitir comentario vacío")
    void crearComentario_shouldRejectEmptyContent() {
        // Arrange
        when(publicacionRepository.findById(1L)).thenReturn(Optional.of(pubGeneral1));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(mockUser1));

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> comentarioService.crearComentarioEnPublicacionGeneral(1L, 1L, "   "));
    }

    @Test
    @DisplayName("Comentar: no exceder 300 caracteres")
    void crearComentario_shouldRejectTooLong() {
        // Arrange
        when(publicacionRepository.findById(2L)).thenReturn(Optional.of(pubGeneral2));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(mockUser2));

        String longText = "x".repeat(301);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> comentarioService.crearComentarioEnPublicacionGeneral(2L, 2L, longText));
    }

    @Test
    @DisplayName("Comentar grupal: sin membresía debe fallar")
    void crearComentarioGrupo_withoutMembership_shouldThrow() {
        // Arrange
        when(publicacionRepository.findById(3L)).thenReturn(Optional.of(pubGrupal));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(mockUser3));

        when(membresiaRepo.existsByUsuario_IdAndGrupo_Id(3L, 7L)).thenReturn(false);

        // Act / Assert
        assertThrows(IllegalArgumentException.class, () -> comentarioService.crearComentarioEnPublicacionGrupo(3L, 3L, "Hola"));
    }
}
