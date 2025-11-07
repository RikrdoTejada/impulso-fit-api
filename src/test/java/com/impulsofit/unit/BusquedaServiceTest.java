package com.impulsofit.unit;

import com.impulsofit.dto.response.BusquedaResponseDTO;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Perfil;
import com.impulsofit.model.Reto;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.repository.RetoRepository;
import com.impulsofit.service.BusquedaService;
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

    @Test
    @DisplayName("Busqueda: solo por deporte debe listar grupos y retos")
    void buscar_withOnlyDeporte_shouldReturnGruposAndRetos() {
        when(grupoRepository.findByDeporte_IdDeporte(1L)).thenReturn(List.of(new Grupo()));
        when(retoRepository.findByDeporteId(1L)).thenReturn(List.of(new Reto()));

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
        when(grupoRepository.buscarPorNombreODeporte("term")).thenReturn(List.of(new Grupo()));
        when(retoRepository.searchByTerm("term")).thenReturn(List.of(new Reto()));
        when(perfilRepository.searchByNombreApellido("term")).thenReturn(List.of(new Perfil()));

        BusquedaResponseDTO res = service.buscar("term", null);
        assertThat(res.grupos()).hasSize(1);
        assertThat(res.usuarios()).hasSize(1);
        assertThat(res.retos()).hasSize(1);
    }
}
