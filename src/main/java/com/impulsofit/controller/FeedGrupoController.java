package com.impulsofit.controller;

import com.impulsofit.dto.response.PublicacionResponseDTO;
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
    public List<PublicacionResponseDTO> obtenerFeed(@PathVariable Long grupoId) {
        return feedService.obtenerFeedDTOPorGrupo(grupoId);
    }

}
