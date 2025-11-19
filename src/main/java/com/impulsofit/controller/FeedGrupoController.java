package com.impulsofit.controller;

import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.service.FeedGrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed-grupo")
@PreAuthorize("hasRole('USER')")
public class FeedGrupoController {

    private final FeedGrupoService feedService;

    @GetMapping("/{grupoId}")
    public List<PublicacionResponseDTO> obtenerFeed(@PathVariable Long grupoId) {
        return feedService.obtenerFeedDTOPorGrupo(grupoId);
    }

}
