package com.impulsofit.unit;

import com.impulsofit.dto.response.BusquedaResponseDTO;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Perfil;
import com.impulsofit.model.Reto;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.repository.RetoRepository;
import com.impulsofit.service.BusquedaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("BusquedaService - Pruebas Unitarias")
class BusquedaServiceTest {

    @Mock
    private GrupoRepository grupoRepository;
    @Mock
    private PerfilRepository perfilRepository;
    @Mock
    private RetoRepository retoRepository;

    @InjectMocks
    private BusquedaService service;

    private Grupo g1;
    private Reto r1;
    private Perfil p1;

    @BeforeEach
    void setUp() {
        g1 = createMockGrupo(1L);
        r1 = createMockReto(1L);
        p1 = createMockPerfil(3L, "Name");
    }

    private Grupo createMockGrupo(Long id) { Grupo g = new Grupo(); g.setIdGrupo(id); return g; }
    private Reto createMockReto(Long id) { Reto r = new Reto(); r.setIdReto(id); return r; }
    private Perfil createMockPerfil(Long id, String name) { Perfil p = new Perfil(); p.setIdPerfil(id); p.setNombre(name); return p; }

    @Test
    @DisplayName("Busqueda: solo por deporte debe listar grupos y retos")
    void buscar_withOnlyDeporte_shouldReturnGruposAndRetos() {
        when(grupoRepository.findByDeporte_IdDeporte(1L)).thenReturn(List.of(g1));
        when(retoRepository.findByDeporteId(1L)).thenReturn(List.of(r1));

        BusquedaResponseDTO res = service.buscar("", 1);
        assertThat(res).isNotNull();
        assertThat(res.grupos()).isNotEmpty();
        assertThat(res.retos()).isNotEmpty();
        assertThat(res.usuarios()).isEmpty();
    }

    @Test
    @DisplayName("Busqueda: termino con caracteres invalidos debe fallar")
    void buscar_withInvalidCharacters_shouldThrow() {
        assertThrows(RuntimeException.class, () -> service.buscar("bad!@#", null));
    }

    @Test
    @DisplayName("Busqueda: termino general debe devolver resultados en todos los tipos")
    void buscar_generalTerm_shouldReturnAllLists() {
        when(grupoRepository.buscarPorNombreODeporte("term")).thenReturn(List.of(g1));
        when(retoRepository.searchByTerm("term")).thenReturn(List.of(r1));
        when(perfilRepository.searchByNombreApellido("term")).thenReturn(List.of(p1));

        BusquedaResponseDTO res = service.buscar("term", null);
        assertThat(res.grupos()).hasSize(1);
        assertThat(res.usuarios()).hasSize(1);
        assertThat(res.retos()).hasSize(1);
    }
}
