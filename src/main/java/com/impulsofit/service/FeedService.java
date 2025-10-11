package com.impulsofit.service;

import com.impulsofit.dto.response.*;
import com.impulsofit.model.Publicacion;
import com.impulsofit.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedService {

    private final PublicacionRepository publicacionRepository;
    private final ComentarioRepository comentarioRepository;
    private final ListaSeguidoRepository listaSeguidoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final GrupoService grupoService;

    public FeedService(PublicacionRepository publicacionRepository,
                       ComentarioRepository comentarioRepository,
                       ListaSeguidoRepository listaSeguidoRepository,
                       MembresiaGrupoRepository membresiaGrupoRepository,
                       GrupoService grupoService) {
        this.publicacionRepository = publicacionRepository;
        this.comentarioRepository = comentarioRepository;
        this.listaSeguidoRepository = listaSeguidoRepository;
        this.membresiaGrupoRepository = membresiaGrupoRepository;
        this.grupoService = grupoService;
    }

    // Método original (solo publicaciones)
    public List<FeedResponseDTO> obtenerFeed(Long idUsuario) {
        return obtenerPublicacionesFeed(idUsuario);
    }

    // Nuevo método que incluye publicaciones + grupos populares
    public FeedCompletoDTO obtenerFeedCompleto(Long idUsuario) {
        List<FeedResponseDTO> publicaciones = obtenerPublicacionesFeed(idUsuario);
        List<GrupoPopularDTO> gruposPopulares = grupoService.obtenerGruposPopularesParaUsuario(idUsuario);

        return new FeedCompletoDTO(publicaciones, gruposPopulares);
    }

    // Método privado para obtener solo publicaciones
    private List<FeedResponseDTO> obtenerPublicacionesFeed(Long idUsuario) {
        List<Long> idsUsuarios = listaSeguidoRepository.findSeguidosIdsPorUsuario(idUsuario);
        List<Long> idsGrupos = membresiaGrupoRepository.findIdsGruposByUsuario(idUsuario);

        List<Publicacion> publicaciones;

        if (idsUsuarios.isEmpty() && idsGrupos.isEmpty()) {
            publicaciones = Collections.emptyList();
        } else {
            publicaciones = publicacionRepository.findFeedByUsuariosAndGrupos(idsUsuarios, idsGrupos);
        }

        if (publicaciones.isEmpty()) {
            publicaciones = publicacionRepository.findAllOrdered();
        }

        return publicaciones.stream().map(p -> {
            FeedResponseDTO dto = new FeedResponseDTO();
            dto.setIdPublicacion(p.getIdPublicacion());
            dto.setNombreUsuario(p.getUsuario().getNombre());
            dto.setContenido(p.getContenido());
            dto.setNombreGrupo(p.getGrupo() != null ? p.getGrupo().getNombre() : null);
            dto.setFechaPublicacion(p.getFechaPublicacion());

            var comentarios = comentarioRepository.findByPublicacion_IdPublicacionOrderByFechaComentarioAsc(p.getIdPublicacion())
                    .stream()
                    .map(c -> {
                        ComentarioResponseDTO cdto = new ComentarioResponseDTO();
                        cdto.setNombreUsuario(c.getUsuario().getNombre());
                        cdto.setContenido(c.getContenido());
                        cdto.setFechaComentario(c.getFechaComentario());
                        return cdto;
                    })
                    .collect(Collectors.toList());

            dto.setComentarios(comentarios);
            return dto;
        }).collect(Collectors.toList());
    }
}