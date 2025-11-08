package com.impulsofit.unit;

import com.impulsofit.dto.request.PerfilRequestDTO;
import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Perfil;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.service.PerfilService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PerfilService - Pruebas Unitarias")
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private PerfilService perfilService;

    private Perfil perfil10;
    private Perfil perfil11;

    @BeforeEach
    void setUp() {
        perfil10 = createMockPerfil(10L, "OldName");
        perfil11 = createMockPerfil(11L, "Name");
    }

    private Perfil createMockPerfil(Long id, String nombre) {
        Perfil p = new Perfil();
        p.setIdPerfil(id);
        p.setNombre(nombre);
        return p;
    }

    @Test
    @DisplayName("Editar perfil: modificar nombre correctamente")
    void actualizarPerfil_withValidName_shouldUpdateAndReturnResponse() {
        when(perfilRepository.findById(10L)).thenReturn(Optional.of(perfil10));
        when(perfilRepository.save(perfil10)).thenReturn(perfil10);

        PerfilRequestDTO req = new PerfilRequestDTO("  NewName  ", "Apellido", "Bio", "Loc", "pic.jpg");
        PerfilResponseDTO res = perfilService.actualizarPerfil(10L, req);

        assertThat(res).isNotNull();
        assertThat(res.nombre()).isEqualTo("NewName");
        assertThat(res.apellido()).isEqualTo("Apellido");
    }

    @Test
    @DisplayName("Editar perfil: no permitir dejar nombre vacío")
    void actualizarPerfil_withEmptyName_shouldThrow() {
        when(perfilRepository.findById(11L)).thenReturn(Optional.of(perfil11));

        PerfilRequestDTO req = new PerfilRequestDTO("   ", "Apellido", "Bio", "Loc", "pic.jpg");
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> perfilService.actualizarPerfil(11L, req));
        assertThat(ex.getMessage()).contains("no puede estar vacío");
    }
}
