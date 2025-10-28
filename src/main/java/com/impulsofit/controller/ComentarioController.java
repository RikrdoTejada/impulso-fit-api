package com.impulsofit.controller;

import com.impulsofit.dto.request.ComentarioRequestDTO;
import com.impulsofit.dto.response.ComentarioResponseDTO;
import com.impulsofit.model.Comentario;
import com.impulsofit.model.Usuario;
import com.impulsofit.service.ComentarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping("/publicacion/{id}")
    public List<ComentarioResponseDTO> listarPorPublicacion(@PathVariable Long id) {
        return comentarioService.listarPorPublicacion(id).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/general/{publicacionId}")
    public List<ComentarioResponseDTO> listarGeneral(@PathVariable Long publicacionId) {
        return comentarioService.listarPorPublicacionGeneral(publicacionId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/grupal/{publicacionId}")
    public List<ComentarioResponseDTO> listarGrupal(@PathVariable Long publicacionId) {
        return comentarioService.listarPorPublicacionGrupo(publicacionId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/general")
    public ComentarioResponseDTO crearEnGeneral(@RequestBody ComentarioRequestDTO dto) {
        Comentario guardado = comentarioService.crearComentarioEnPublicacionGeneral(dto.getUsuarioId(), dto.getPublicacionId(), dto.getContenido());
        return toDto(guardado);
    }

    @PostMapping("/grupal")
    public ComentarioResponseDTO crearEnGrupal(@RequestBody ComentarioRequestDTO dto) {
        Comentario guardado = comentarioService.crearComentarioEnPublicacionGrupo(dto.getUsuarioId(), dto.getPublicacionId(), dto.getContenido());
        return toDto(guardado);
    }

    @DeleteMapping("/{id}")
    public void eliminarComentario(@PathVariable Long id) {
        comentarioService.eliminarComentario(id);
    }

    private ComentarioResponseDTO toDto(Comentario c) {
        return new ComentarioResponseDTO(
                c.getId(),
                c.getContenido(),
                c.getUsuario() != null ? c.getUsuario().getNombre() : null,
                c.getFechaCreacion()
        );
    }
}
