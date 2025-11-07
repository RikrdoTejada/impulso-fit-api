package com.impulsofit.unit;

import com.impulsofit.dto.request.PerfilRequestDTO;
import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Perfil;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.service.PerfilService;
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

    @Test
    @DisplayName("Editar perfil: modificar nombre correctamente")
    void actualizarPerfil_withValidName_shouldUpdateAndReturnResponse() {
        Perfil p = new Perfil();
        p.setIdPerfil(10L);
        p.setNombre("OldName");

        when(perfilRepository.findById(10L)).thenReturn(Optional.of(p));
        when(perfilRepository.save(p)).thenReturn(p);

        PerfilRequestDTO req = new PerfilRequestDTO("  NewName  ", "Apellido", "Bio", "Loc", "pic.jpg");
        PerfilResponseDTO res = perfilService.actualizarPerfil(10L, req);

        assertThat(res).isNotNull();
        assertThat(res.nombre()).isEqualTo("NewName");
        assertThat(res.apellido()).isEqualTo("Apellido");
    }

    @Test
    @DisplayName("Editar perfil: no permitir dejar nombre vacío")
    void actualizarPerfil_withEmptyName_shouldThrow() {
        Perfil p = new Perfil();
        p.setIdPerfil(11L);
        p.setNombre("Name");
        when(perfilRepository.findById(11L)).thenReturn(Optional.of(p));

        PerfilRequestDTO req = new PerfilRequestDTO("   ", "Apellido", "Bio", "Loc", "pic.jpg");
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> perfilService.actualizarPerfil(11L, req));
        assertThat(ex.getMessage()).contains("no puede estar vacío");
    }
}
