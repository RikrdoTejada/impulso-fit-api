package com.impulsofit.unit;

import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.ParticipacionReto;
import com.impulsofit.model.RegistroProceso;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.ParticipacionRetoRepository;
import com.impulsofit.repository.RegistroProcesoRepository;
import com.impulsofit.service.ParticipacionRetoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ParticipacionRetoService - Pruebas Unitarias")
class ParticipacionRetoServiceTest {

    @Mock
    private ParticipacionRetoRepository participacionRetoRepository;
    @Mock
    private RegistroProcesoRepository registroProcesoRepository;
    @Mock
    private MembresiaGrupoRepository membresiaRepo;

    @InjectMocks
    private ParticipacionRetoService service;

    private Usuario u1;
    private Usuario u2;
    private Reto r1;
    private Reto r2;

    @BeforeEach
    void setUp() {
        u1 = createMockUser(1L);
        u2 = createMockUser(2L);

        r1 = createMockReto(1L, createMockGrupo(5L));
        r2 = createMockReto(2L, createMockGrupo(6L));
    }

    private Usuario createMockUser(Long id) {
        Usuario u = new Usuario(); u.setIdUsuario(id); return u;
    }

    private com.impulsofit.model.Grupo createMockGrupo(Long id) {
        com.impulsofit.model.Grupo g = new com.impulsofit.model.Grupo(); g.setIdGrupo(id); return g;
    }

    private Reto createMockReto(Long id, com.impulsofit.model.Grupo grupo) {
        Reto r = new Reto(); r.setIdReto(id); r.setGrupo(grupo); return r;
    }

    @Test
    @DisplayName("Unirse/Abandonar reto: usuario no miembro debe fallar")
    void toggleParticipation_userNotMember_shouldThrow() {
        when(membresiaRepo.existsByUsuario_IdUsuarioAndGrupo_IdGrupo(1L, 5L)).thenReturn(false);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> service.toggleParticipation(u1, r1));
        assertThat(ex.getMessage()).contains("debe ingresar al grupo");
    }

    @Test
    @DisplayName("Unirse y abandonar reto: flujo básico")
    void toggleParticipation_joinAndLeave_shouldCreateAndThenDelete() {
        when(membresiaRepo.existsByUsuario_IdUsuarioAndGrupo_IdGrupo(2L, 6L)).thenReturn(true);
        when(participacionRetoRepository.findByRetoAndUsuario(r2, u2)).thenReturn(Optional.empty());

        boolean joined = service.toggleParticipation(u2, r2);
        assertThat(joined).isTrue();
        verify(participacionRetoRepository).save(any(ParticipacionReto.class));

        ParticipacionReto p = new ParticipacionReto(); p.setIdReto(r2.getIdReto()); p.setIdUsuario(u2.getIdUsuario());
        when(participacionRetoRepository.findByRetoAndUsuario(r2, u2)).thenReturn(Optional.of(p));
        when(registroProcesoRepository.findByParticipacionRetoOrderByFechaDesc(p)).thenReturn(List.of(new RegistroProceso()));

        boolean now = service.toggleParticipation(u2, r2);
        assertThat(now).isFalse();
        verify(registroProcesoRepository).deleteAll(anyList());
        verify(participacionRetoRepository).delete(p);
    }

    @Test
    @DisplayName("Ranking: debe ordenar participantes por total")
    void rankingDto_shouldReturnOrderedProgreso() {
        Reto r = new Reto(); r.setIdReto(9L); r.setObjetivoTotal(100.0);
        RegistroProceso rp1 = new RegistroProceso();
        ParticipacionReto p1 = new ParticipacionReto(); p1.setIdUsuario(10L); p1.setIdReto(9L);
        rp1.setParticipacionReto(p1); rp1.setAvance("30");
        RegistroProceso rp2 = new RegistroProceso();
        ParticipacionReto p2 = new ParticipacionReto(); p2.setIdUsuario(11L); p2.setIdReto(9L);
        rp2.setParticipacionReto(p2); rp2.setAvance("70");
        when(registroProcesoRepository.findByParticipacionReto_Reto(r)).thenReturn(List.of(rp1, rp2));

        var ranking = service.rankingDto(r);
        assertThat(ranking).hasSize(2);
        assertThat(ranking.get(0).idUsuario()).isEqualTo(11L);
        assertThat(ranking.get(1).idUsuario()).isEqualTo(10L);
    }
}
