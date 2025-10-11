package com.impulsofit.controller;

import com.impulsofit.dto.response.FeedCompletoDTO;
import com.impulsofit.dto.response.FeedResponseDTO;
import com.impulsofit.service.FeedService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    // Endpoint original (solo publicaciones)
    @GetMapping("/{idUsuario}")
    public List<FeedResponseDTO> obtenerFeed(@PathVariable Long idUsuario) {
        return feedService.obtenerFeed(idUsuario);
    }

    // Nuevo endpoint (publicaciones + grupos populares)
    @GetMapping("/completo/{idUsuario}")
    public FeedCompletoDTO obtenerFeedCompleto(@PathVariable Long idUsuario) {
        return feedService.obtenerFeedCompleto(idUsuario);
    }
}