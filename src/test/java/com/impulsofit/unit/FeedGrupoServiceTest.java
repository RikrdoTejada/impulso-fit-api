package com.impulsofit.unit;

import com.impulsofit.model.Publicacion;
import com.impulsofit.model.PublicacionType;
import com.impulsofit.model.Grupo;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.service.FeedGrupoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeedGrupoService - Pruebas Unitarias")
class FeedGrupoServiceTest {

    @Mock
    private PublicacionRepository publicacionRepository;

    @InjectMocks
    private FeedGrupoService feedGrupoService;

    private Publicacion pubGrupo;

    @BeforeEach
    void setUp() {
        Grupo grupo2 = new Grupo();
        grupo2.setIdGrupo(2L);
        pubGrupo = createMockPublicacion(5L, "Hola grupo", PublicacionType.GROUP, grupo2);
    }

    private Publicacion createMockPublicacion(Long id, String contenido, PublicacionType type, Grupo grupo) {
        Publicacion p = new Publicacion();
        p.setIdPublicacion(id);
        p.setContenido(contenido);
        p.setType(type);
        p.setGrupo(grupo);
        p.setFechaPublicacion(LocalDateTime.now());
        return p;
    }

    @Test
    @DisplayName("Feed de grupo: visualización de publicaciones grupales")
    void obtenerFeedPorGrupo_shouldReturnPublicaciones() {
        when(publicacionRepository.findAllByTypeAndGrupo_IdGrupo(PublicacionType.GROUP, 2L)).thenReturn(List.of(pubGrupo));

        List<Publicacion> res = feedGrupoService.obtenerFeedPorGrupo(2L);
        assertThat(res).isNotEmpty();
        assertThat(res).extracting(Publicacion::getContenido).containsExactly("Hola grupo");
    }
}
