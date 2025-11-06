package com.impulsofit.service;

import com.impulsofit.dto.response.MembresiaGrupoResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.MembresiaGrupo;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class GrupoMembresiaService {

    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;


    // Unirse a grupo
    @Transactional
    public MembresiaGrupoResponseDTO unirseAGrupo(Long idUsuario, Long idGrupo) {
        // Verificar si ya es miembro
        if (membresiaGrupoRepository.existsByUsuarioIdUsuarioAndGrupoIdGrupo(idUsuario, idGrupo)) {
            throw new AlreadyExistsException("El usuario ya pertenece a este grupo.");
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));

        MembresiaGrupo membresiaEntity = new MembresiaGrupo();
        membresiaEntity.setUsuario(usuario);
        membresiaEntity.setGrupo(grupo);

        MembresiaGrupo saved = membresiaGrupoRepository.save(membresiaEntity);

        LocalDate fechaUnionLocalDate = (saved.getFechaUnion() == null) ? null : saved.getFechaUnion().toLocalDate();

        // Actualizar contador de miembros
        grupo.setCantidadMiembros(grupo.getCantidadMiembros() + 1);
        grupoRepository.save(grupo);

        return new MembresiaGrupoResponseDTO(
                saved.getIdMembresia(),
                saved.getUsuario().getNombres(),
                saved.getGrupo().getNombre(),
                fechaUnionLocalDate
        );
    }

    // Dejar grupo
    @Transactional
    public void dejarGrupo(Long idUsuario, Long idGrupo) {
        MembresiaGrupo membresia = membresiaGrupoRepository.findByUsuarioIdUsuarioAndGrupoIdGrupo(idUsuario, idGrupo)
                .orElseThrow(() -> new IllegalArgumentException("No eres miembro de este grupo"));

        membresiaGrupoRepository.delete(membresia);

        // Actualizar contador de miembros
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
        grupo.setCantidadMiembros(Math.max(0, grupo.getCantidadMiembros() - 1));
        grupoRepository.save(grupo);
    }

    // Verificar membresía
    public boolean esMiembroDeGrupo(Long idUsuario, Long idGrupo) {
        return membresiaGrupoRepository.existsByUsuarioIdUsuarioAndGrupoIdGrupo(idUsuario, idGrupo);
    }
}