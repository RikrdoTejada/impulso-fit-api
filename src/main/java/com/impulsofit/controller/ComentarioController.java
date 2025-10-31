package com.impulsofit.controller;

import com.impulsofit.dto.request.ComentarioRequestDTO;
import com.impulsofit.dto.response.ComentarioResponseDTO;
import com.impulsofit.model.Comentario;
import com.impulsofit.service.ComentarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping("/publicacion/{id}")
    public List<ComentarioResponseDTO> listarPorPublicacion(@PathVariable Long id) {
        return comentarioService.listarPorPublicacionDTO(id);
    }

    @GetMapping("/general/{publicacionId}")
    public List<ComentarioResponseDTO> listarGeneral(@PathVariable Long publicacionId) {
        return comentarioService.listarPorPublicacionGeneralDTO(publicacionId);
    }

    @GetMapping("/grupal/{publicacionId}")
    public List<ComentarioResponseDTO> listarGrupal(@PathVariable Long publicacionId) {
        return comentarioService.listarPorPublicacionGrupoDTO(publicacionId);
    }

    @PostMapping("/general")
    public ComentarioResponseDTO crearEnGeneral(@RequestBody ComentarioRequestDTO dto) {
        Comentario guardado = comentarioService.crearComentarioEnPublicacionGeneral(dto.getUsuarioId(), dto.getPublicacionId(), dto.getContenido());
        return comentarioService.listarPorPublicacionDTO(guardado.getPublicacion().getId())
                .stream().filter(c -> c.getId().equals(guardado.getId())).findFirst().orElse(null);
    }

    @PostMapping("/grupal")
    public ComentarioResponseDTO crearEnGrupal(@RequestBody ComentarioRequestDTO dto) {
        Comentario guardado = comentarioService.crearComentarioEnPublicacionGrupo(dto.getUsuarioId(), dto.getPublicacionId(), dto.getContenido());
        return comentarioService.listarPorPublicacionDTO(guardado.getPublicacion().getId())
                .stream().filter(c -> c.getId().equals(guardado.getId())).findFirst().orElse(null);
    }

    @DeleteMapping("/{id}")
    public void eliminarComentario(@PathVariable Long id) {
        comentarioService.eliminarComentario(id);
    }
}
