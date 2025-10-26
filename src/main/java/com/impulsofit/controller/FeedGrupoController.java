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
        return feedService.obtenerFeedPorGrupo(grupoId).stream()
                .map(pub -> {
                    String autorNombre = (pub.getAutor() != null) ? pub.getAutor().getNombre() : null;
                    List<String> comentarios = (pub.getComentarios() == null)
                            ? Collections.emptyList()
                            : pub.getComentarios().stream()
                                .map(Comentario::getContenido)
                                .collect(Collectors.toList());
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
