package com.impulsofit.controller;

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

    @GetMapping("/{idUsuario}")
    public List<FeedResponseDTO> obtenerFeed(@PathVariable Long idUsuario) {
        return feedService.obtenerFeed(idUsuario);
    }
}
