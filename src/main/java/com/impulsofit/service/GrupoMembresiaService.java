package com.impulsofit.service;

import com.impulsofit.model.Grupo;
import com.impulsofit.model.MembresiaGrupo;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GrupoMembresiaService {

    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;

    public GrupoMembresiaService(MembresiaGrupoRepository membresiaGrupoRepository,
                                 UsuarioRepository usuarioRepository,
                                 GrupoRepository grupoRepository) {
        this.membresiaGrupoRepository = membresiaGrupoRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
    }

    // Unirse a grupo
    @Transactional
    public String unirseAGrupo(Long idUsuario, Long idGrupo) {
        // Verificar si ya es miembro
        if (membresiaGrupoRepository.existsByUsuarioIdUsuarioAndGrupoIdGrupo(idUsuario, idGrupo)) {
            return "Ya eres miembro de este grupo";
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        MembresiaGrupo membresia = new MembresiaGrupo(usuario, grupo);
        membresiaGrupoRepository.save(membresia);

        // Actualizar contador de miembros
        grupo.setCantidadMiembros(grupo.getCantidadMiembros() + 1);
        grupoRepository.save(grupo);

        return "Te has unido al grupo exitosamente";
    }

    // Dejar grupo
    @Transactional
    public String dejarGrupo(Long idUsuario, Long idGrupo) {
        MembresiaGrupo membresia = membresiaGrupoRepository.findByUsuarioIdUsuarioAndGrupoIdGrupo(idUsuario, idGrupo)
                .orElseThrow(() -> new RuntimeException("No eres miembro de este grupo"));

        membresiaGrupoRepository.delete(membresia);

        // Actualizar contador de miembros
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));
        grupo.setCantidadMiembros(Math.max(0, grupo.getCantidadMiembros() - 1));
        grupoRepository.save(grupo);

        return "Has dejado el grupo exitosamente";
    }

    // Verificar membresía
    public boolean esMiembroDeGrupo(Long idUsuario, Long idGrupo) {
        return membresiaGrupoRepository.existsByUsuarioIdUsuarioAndGrupoIdGrupo(idUsuario, idGrupo);
    }
}