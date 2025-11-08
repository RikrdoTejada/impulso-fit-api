package com.impulsofit.unit;


import com.impulsofit.dto.request.RetoRequestDTO;
import com.impulsofit.dto.response.RetoResponseDTO;
import com.impulsofit.model.*;
import com.impulsofit.repository.*;
import com.impulsofit.service.RetoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RetoServiceTest {
    @Mock
    private RetoRepository retoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private GrupoRepository grupoRepository;
    @Mock
    private UnidadRepository unidadRepository;
    @Mock
    private MembresiaGrupoRepository membresiaGrupoRepository;

    @InjectMocks
    private RetoService retoService;

    private Reto r1;
    private Grupo g1;
    private Usuario u1;
    private Unidad un1;
    private MembresiaGrupo m1;

    @BeforeEach
    void setUp() {
        r1 = createMockreto(2L, "Rutina Mortal");
        g1 = createMockGrupo(1L);
        u1 = createMockUsuario(1L);
        un1 = createMockUnidad(1L);
        m1 = createMockMembresia(1L,u1,g1);
    }

    private Reto createMockreto(Long id, String titulo) {Reto reto = new Reto(); reto.setIdReto(id); reto.setTitulo(titulo); return reto;}
    private Grupo createMockGrupo(Long id){Grupo p = new Grupo();p.setId(id);return p;}
    private Usuario createMockUsuario(Long id){Usuario p = new Usuario();p.setId(id);return p;}
    private Unidad createMockUnidad(Long id){Unidad p = new Unidad(); p.setIdUnidad(id);return p;}
    private MembresiaGrupo createMockMembresia(Long id, Usuario usuario, Grupo grupo) {MembresiaGrupo m = new MembresiaGrupo();m.setId(id); m.setUsuario(u1); m.setGrupo(g1);return m;}

    @Test
    @DisplayName("Debe crear un reto exitosamente cuando el nombre no existe y las fechas son valida")
    void    crearReto_validData_Succes(){
        //Arrange
        RetoRequestDTO req = new RetoRequestDTO(
                g1.getIdGrupo(), u1.getIdUsuario(), un1.getIdUnidad(), "Dominadas mortales",
                "Hacer 100 dominadas",
                100.00,
                LocalDate.parse("2025-11-07"),
                LocalDate.parse("2025-11-08")
        );

        Reto reto = new Reto(
                1L , g1, u1, un1, "Dominadas mortales",
                "Hacer 100 dominadas",
                100.00,
                LocalDate.parse("2025-11-07"),
                LocalDate.parse("2025-11-07"),
                LocalDate.parse("2025-11-08")
        );

        when(retoRepository.existsByTituloIgnoreCaseAndGrupo_IdGrupo("Dominadas mortales",1L)).thenReturn(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(grupoRepository.findById(1L)).thenReturn(Optional.of(g1));
        when(unidadRepository.findById(1L)).thenReturn(Optional.of(un1));
        when(membresiaGrupoRepository.existsByUsuario_IdUsuarioAndGrupo_IdGrupo(1L, 1L)).thenReturn(true);
        when(retoRepository.save(any(Reto.class))).thenReturn(reto);

        //Actions
        RetoResponseDTO response = retoService.create(req);

        //Assert
        assertThat(response).isNotNull();
        assertThat(response.id_reto()).isEqualTo(1L);

    }
}
