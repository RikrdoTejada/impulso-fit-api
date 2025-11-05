package com.impulsofit.service;

import com.impulsofit.dto.request.GrupoRequestDTO;
import com.impulsofit.dto.response.GrupoPopularDTO;
import com.impulsofit.dto.response.GrupoResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Deporte;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.DeporteRepository;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GrupoService {
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final DeporteRepository deporteRepository;

    // Crear un nuevo grupo
    public GrupoResponseDTO create(GrupoRequestDTO grupo) {

        Usuario usuario = usuarioRepository.findById(grupo.id_usuario_creador())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado"));

        Deporte deporte = deporteRepository.findById(grupo.id_deporte())
                .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado"));

        Grupo grupoEntity = new Grupo();
        grupoEntity.setCreador(usuario); // Alias para usuarioCreador
        grupoEntity.setDeporte(deporte);
        grupoEntity.setNombre(grupo.nombre());
        grupoEntity.setDescripcion(grupo.descripcion());
        grupoEntity.setUbicacion(grupo.ubicacion());

        Grupo saved = grupoRepository.save(grupoEntity);
        return new GrupoResponseDTO(
                saved.getIdGrupo(),
                saved.getNombre(),
                saved.getDeporte() != null ? saved.getDeporte().getNombre() : null,
                saved.getDescripcion(),
                "/grupos/" + saved.getIdGrupo() + "/unirse",
                saved.getUbicacion(),
                (saved.getFechaCreacion() == null) ? null : saved.getFechaCreacion().toLocalDate()
        );
    }

    // Borrar grupo por id
    public void delete(Long id) {
        if (!grupoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el grupo con el id: " + id);
        }
        grupoRepository.deleteById(id);
    }

    // Grupos populares para todos los usuarios
    public List<GrupoPopularDTO> obtenerGruposPopulares() {
        List<Grupo> grupos = grupoRepository.findTop5ByOrderByCantidadMiembrosDesc();

        return grupos.stream()
                .map(this::convertirAGrupoPopularDTO)
                .collect(Collectors.toList());
    }

    // Grupos populares que el usuario NO pertenece (recomendaciones personalizadas)
    public List<GrupoPopularDTO> obtenerGruposPopularesParaUsuario(Long idUsuario) {
        // Grupos a los que el usuario ya pertenece
        List<Long> gruposUsuario = membresiaGrupoRepository.findIdsGruposByUsuario(idUsuario);

        List<Grupo> grupos;

        if (gruposUsuario.isEmpty()) {
            // Si no pertenece a ningún grupo, mostrar los más populares
            grupos = grupoRepository.findTop5ByOrderByCantidadMiembrosDesc();
        } else {
            // Mostrar grupos populares a los que NO pertenece
            grupos = grupoRepository.findGruposPopularesNoPertenece(gruposUsuario);

            // Si no hay suficientes, complementar con grupos populares generales
            if (grupos.size() < 3) {
                List<Grupo> gruposPopulares = grupoRepository.findTop5ByOrderByCantidadMiembrosDesc();
                grupos.addAll(gruposPopulares);
            }
        }

        // Limitar a 5 grupos
        if (grupos.size() > 5) {
            grupos = grupos.subList(0, 5);
        }

        return grupos.stream()
                .map(this::convertirAGrupoPopularDTO)
                .collect(Collectors.toList());
    }

    private GrupoPopularDTO convertirAGrupoPopularDTO(Grupo grupo) {
          return new GrupoPopularDTO(
            grupo.getIdGrupo(),
            grupo.getNombre(),
            grupo.getDescripcion(),
            grupo.getFotoGrupo(),
            grupo.getCantidadMiembros() != null ? grupo.getCantidadMiembros() : 0);
    }
}