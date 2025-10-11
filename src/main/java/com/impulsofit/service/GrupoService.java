package com.impulsofit.service;

import com.impulsofit.dto.response.GrupoPopularDTO;
import com.impulsofit.model.Grupo;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;

    public GrupoService(GrupoRepository grupoRepository,
                        MembresiaGrupoRepository membresiaGrupoRepository) {
        this.grupoRepository = grupoRepository;
        this.membresiaGrupoRepository = membresiaGrupoRepository;
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
        GrupoPopularDTO dto = new GrupoPopularDTO();
        dto.setIdGrupo(grupo.getIdGrupo());
        dto.setNombre(grupo.getNombre());
        dto.setDescripcion(grupo.getDescripcion());
        dto.setFotoGrupo(grupo.getFotoGrupo());
        dto.setCantidadMiembros(grupo.getCantidadMiembros() != null ? grupo.getCantidadMiembros() : 0);
        return dto;
    }
}