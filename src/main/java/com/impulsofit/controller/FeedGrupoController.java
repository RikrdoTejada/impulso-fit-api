package com.impulsofit.controller;

import com.impulsofit.dto.response.PublicacionGrupoResponseDTO;
import com.impulsofit.model.Comentario;
import com.impulsofit.model.PublicacionGrupo;
import com.impulsofit.service.FeedGrupoService;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feed-grupo")
public class FeedGrupoController {

    private final FeedGrupoService feedService;

    public FeedGrupoController(FeedGrupoService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/{grupoId}")
    public List<PublicacionGrupoResponseDTO> obtenerFeed(@PathVariable Long grupoId) {
        List<PublicacionGrupo> publicaciones = feedService.obtenerFeedPorGrupo(grupoId);
        return publicaciones.stream()
                .map(pub -> {
                    String autorNombre = (pub.getAutor() != null) ? pub.getAutor().getNombre() : null;
                    List<Comentario> comentariosEnt = feedService.obtenerComentariosPorPublicacion(pub.getIdPublicacion());
                    List<String> comentarios = (comentariosEnt == null || comentariosEnt.isEmpty())
                            ? Collections.emptyList()
                            : comentariosEnt.stream().map(Comentario::getContenido).collect(Collectors.toList());
                    return new PublicacionGrupoResponseDTO(
                            pub.getIdPublicacion(),
                            pub.getContenido(),
                            autorNombre,
                            pub.getFechaCreacion(),
                            comentarios
                    );
                })
                .collect(Collectors.toList());
    }

}
