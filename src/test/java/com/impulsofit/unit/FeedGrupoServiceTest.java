package com.impulsofit.unit;

import com.impulsofit.model.Publicacion;
import com.impulsofit.model.PublicacionType;
import com.impulsofit.model.Grupo;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.service.FeedGrupoService;
import jakarta.persistence.EntityManager;
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

    @Mock
    private EntityManager em;

    @InjectMocks
    private FeedGrupoService feedGrupoService;

    @Test
    @DisplayName("Feed de grupo: visualización de publicaciones grupales")
    void obtenerFeedPorGrupo_shouldReturnPublicaciones() {
        Publicacion p = new Publicacion();
        p.setIdPublicacion(5L);
        p.setContenido("Hola grupo");
        p.setType(PublicacionType.GROUP);
        Grupo g = new Grupo();
        g.setIdGrupo(2L);
        p.setGrupo(g);
        p.setFechaPublicacion(LocalDateTime.now());

        when(publicacionRepository.findAllByTypeAndGrupo_IdGrupo(PublicacionType.GROUP, 2L)).thenReturn(List.of(p));

        List<Publicacion> res = feedGrupoService.obtenerFeedPorGrupo(2L);
        assertThat(res).isNotEmpty();
        assertThat(res.get(0).getContenido()).isEqualTo("Hola grupo");
    }
}
