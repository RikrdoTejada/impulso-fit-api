package com.impulsofit.service;

import com.impulsofit.model.*;
import com.impulsofit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RetoService {

    private final RetoRepository retoRepository;
    private final ParticipacionRetoRepository participacionRetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final GrupoMembresiaService grupoMembresiaService;

    // Unirse a reto
    @Transactional
    public String unirseAReto(Long idUsuario, Long idReto) {
        // Verificar si ya participa
        if (participacionRetoRepository.existsByRetoIdRetoAndUsuarioIdUsuario(idReto, idUsuario)) {
            return "Ya participas en este reto";
        }

        Reto reto = retoRepository.findById(idReto)
                .orElseThrow(() -> new RuntimeException("Reto no encontrado"));

        // Verificar si es miembro del grupo
        if (!grupoMembresiaService.esMiembroDeGrupo(idUsuario, reto.getGrupo().getIdGrupo())) {
            return "Debes unirte al grupo primero para participar en este reto";
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // ✅ NUEVA ESTRUCTURA - Usar IDs directos en lugar de objetos
        ParticipacionReto participacion = new ParticipacionReto();
        participacion.setIdReto(reto.getIdReto());
        participacion.setIdUsuario(usuario.getIdUsuario());
        participacion.setFechaUnion(LocalDateTime.now());

        participacionRetoRepository.save(participacion);

        return "Te has unido al reto exitosamente";
    }

    // Abandonar reto
    @Transactional
    public String abandonarReto(Long idUsuario, Long idReto) {
        // ✅ BUSCAR por IDs directos (nueva estructura)
        ParticipacionReto participacion = participacionRetoRepository.findByRetoIdRetoAndUsuarioIdUsuario(idReto, idUsuario)
                .orElseThrow(() -> new RuntimeException("No participas en este reto"));

        participacionRetoRepository.delete(participacion);
        return "Has abandonado el reto exitosamente";
    }

    // Verificar participación en reto
    public boolean participaEnReto(Long idUsuario, Long idReto) {
        return participacionRetoRepository.existsByRetoIdRetoAndUsuarioIdUsuario(idReto, idUsuario);
    }

    // Obtener retos de un grupo
    public List<Reto> obtenerRetosDeGrupo(Long idGrupo) {
        return retoRepository.findByGrupoIdGrupo(idGrupo);
    }

    // Crear reto de prueba
    @Transactional
    public String crearRetoDePrueba(Long idGrupo) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        Usuario usuario = usuarioRepository.findById(28L) // Usuario por defecto
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Reto reto = new Reto();
        reto.setGrupo(grupo);
        reto.setUsuario(usuario);
        reto.setTitulo("Reto de 30 días de Fitness");
        reto.setDescripcion("Completa 30 días consecutivos de ejercicio");
        reto.setFechaInicio(LocalDate.now());
        reto.setFechaFin(LocalDate.now().plusDays(30));

        retoRepository.save(reto);
        return "Reto de prueba creado exitosamente";
    }
}