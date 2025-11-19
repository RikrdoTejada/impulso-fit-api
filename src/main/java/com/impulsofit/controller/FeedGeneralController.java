package com.impulsofit.controller;

import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.service.FeedGeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
@PreAuthorize("hasRole('USER')")
public class FeedGeneralController {

    private final FeedGeneralService feedService;

    @GetMapping("/{idPerfil}")
    public List<PublicacionResponseDTO> obtenerFeedGeneral(@PathVariable Long idPerfil) {
        return feedService.obtenerFeedGeneralPorPerfil(idPerfil);
    }
}

