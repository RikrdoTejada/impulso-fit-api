package com.impulsofit.controller;

import com.impulsofit.dto.response.PublicacionGrupoResponseDTO;
import com.impulsofit.service.FeedGrupoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feed-grupo")
public class FeedGrupoController {

    private final FeedGrupoService feedService;

    public FeedGrupoController(FeedGrupoService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/{grupoId}")
    public List<PublicacionGrupoResponseDTO> obtenerFeed(@PathVariable Long grupoId) {
        // Usar la versión DTO que no depende de la herencia JOINED
        return feedService.obtenerFeedDTOPorGrupo(grupoId);
    }

}
