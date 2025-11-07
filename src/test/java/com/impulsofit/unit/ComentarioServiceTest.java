package com.impulsofit.unit;

import com.impulsofit.model.Comentario;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.PublicacionType;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.ComentarioRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.repository.UsuarioRepository;
import com.impulsofit.service.ComentarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ComentarioService - Pruebas Unitarias")
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;
    @Mock
    private PublicacionRepository publicacionRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private MembresiaGrupoRepository membresiaRepo;

    @InjectMocks
    private ComentarioService comentarioService;

    @Test
    @DisplayName("Comentar: no permitir comentario vacío")
    void crearComentario_shouldRejectEmptyContent() {
        Publicacion pub = new Publicacion();
        pub.setIdPublicacion(1L);
        pub.setType(PublicacionType.GENERAL);
        when(publicacionRepository.findById(1L)).thenReturn(Optional.of(pub));

        Usuario u = new Usuario();
        u.setIdUsuario(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        assertThrows(IllegalArgumentException.class, () -> comentarioService.crearComentarioEnPublicacionGeneral(1L, 1L, "   "));
    }

    @Test
    @DisplayName("Comentar: no exceder 300 caracteres")
    void crearComentario_shouldRejectTooLong() {
        Publicacion pub = new Publicacion();
        pub.setIdPublicacion(2L);
        pub.setType(PublicacionType.GENERAL);
        when(publicacionRepository.findById(2L)).thenReturn(Optional.of(pub));

        Usuario u = new Usuario();
        u.setIdUsuario(2L);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(u));

        String longText = "x".repeat(301);
        assertThrows(IllegalArgumentException.class, () -> comentarioService.crearComentarioEnPublicacionGeneral(2L, 2L, longText));
    }

    @Test
    @DisplayName("Comentar grupal: sin membresía debe fallar")
    void crearComentarioGrupo_withoutMembership_shouldThrow() {
        Publicacion pub = new Publicacion();
        pub.setIdPublicacion(3L);
        pub.setType(PublicacionType.GROUP);
        var g = new com.impulsofit.model.Grupo();
        g.setIdGrupo(7L);
        pub.setGrupo(g);
        when(publicacionRepository.findById(3L)).thenReturn(Optional.of(pub));

        Usuario u = new Usuario();
        u.setIdUsuario(3L);
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(u));

        when(membresiaRepo.existsByUsuario_IdAndGrupo_Id(3L, 7L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> comentarioService.crearComentarioEnPublicacionGrupo(3L, 3L, "Hola"));
    }
}
