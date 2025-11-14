package com.impulsofit.controller;

import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.dto.response.SeguidoResponseDTO;
import com.impulsofit.service.SeguidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class SeguidoController {

    private final SeguidoService seguidoService;

    @PostMapping("/{idPerfilSeguidor}/seguir/{idPerfilSeguido}")
    public SeguidoResponseDTO alternarSeguir(@PathVariable Long idPerfilSeguidor, @PathVariable Long idPerfilSeguido) {
        return seguidoService.alternarSeguir(idPerfilSeguidor, idPerfilSeguido);
    }

    @GetMapping("/{idPerfil}/seguidos")
    public List<PerfilResponseDTO> obtenerSeguidos(@PathVariable Long idPerfil) {
        return seguidoService.listarSeguidos(idPerfil);
    }

    @GetMapping("/{idPerfil}/seguidores")
    public List<PerfilResponseDTO> obtenerSeguidores(@PathVariable Long idPerfil) {
        return seguidoService.listarSeguidores(idPerfil);
    }
}