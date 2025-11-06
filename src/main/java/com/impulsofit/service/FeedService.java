package com.impulsofit.service;

import com.impulsofit.dto.response.*;
import com.impulsofit.model.Comentario;
import com.impulsofit.model.Publicacion;
import com.impulsofit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class FeedService {

    private final PublicacionRepository publicacionRepository;
    private final ComentarioRepository comentarioRepository;
    private final ListaSeguidoRepository listaSeguidoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final GrupoService grupoService;

    // Metodo original (solo publicaciones)
    public List<FeedResponseDTO> obtenerFeed(Long idUsuario) {
        return obtenerPublicacionesFeed(idUsuario);
    }

    // Nuevo metodo que incluye publicaciones + grupos populares
    public FeedCompletoDTO obtenerFeedCompleto(Long idUsuario) {
        List<FeedResponseDTO> publicaciones = obtenerPublicacionesFeed(idUsuario);
        List<GrupoPopularDTO> gruposPopulares = grupoService.obtenerGruposPopularesParaUsuario(idUsuario);

        return new FeedCompletoDTO(publicaciones, gruposPopulares);
    }

    // Metodo privado para obtener solo publicaciones
    private List<FeedResponseDTO> obtenerPublicacionesFeed(Long idUsuario) {

        List<Long> idsUsuarios = Optional
                .ofNullable(listaSeguidoRepository.findSeguidosIdsPorUsuario(idUsuario))
                .orElseGet(Collections::emptyList);

        List<Long> idsGrupos = Optional
                .ofNullable(membresiaGrupoRepository.findIdsGruposByUsuario(idUsuario))
                .orElseGet(Collections::emptyList);


        List<Publicacion> publicaciones;
        if (idsUsuarios.isEmpty() && idsGrupos.isEmpty()) {
            publicaciones = publicacionRepository.findAllOrdered(); // fallback directo
        } else {
            publicaciones = publicacionRepository.findFeedByUsuariosAndGrupos(idsUsuarios, idsGrupos);
            if (publicaciones.isEmpty()) {
                publicaciones = publicacionRepository.findAllOrdered(); // fallback si no hay nada relevante
            }
        }
        if (publicaciones.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> pubIds = publicaciones.stream()
                .map(Publicacion::getIdPublicacion)
                .filter(Objects::nonNull)
                .toList();

        List<Comentario> comentarios = comentarioRepository
                .findByPublicacionIdInOrderByFechaCreacionAsc(pubIds);

        Map<Long, List<ComentarioResponseDTO>> comentariosPorPublicacion = comentarios
                .stream()
                .collect(Collectors.groupingBy(
                        c -> c.getPublicacion().getIdPublicacion(),
                        Collectors.mapping(c -> new ComentarioResponseDTO(
                                c.getUsuario() != null ? c.getUsuario().getNombres() : null,
                                c.getContenido(),
                                c.getFechaCreacion() // LocalDateTime
                        ), Collectors.toList())
                ));

        return publicaciones.stream()
                .map(p -> new FeedResponseDTO(
                        p.getIdPublicacion(),
                        p.getUsuario() != null ? p.getUsuario().getNombres() : null,
                        p.getContenido(),
                        p.getGrupo() != null ? p.getGrupo().getNombre() : null,
                        p.getFechaPublicacion(), // LocalDateTime
                        comentariosPorPublicacion.getOrDefault(p.getIdPublicacion(), Collections.emptyList())
                ))
                .toList();
    }
}