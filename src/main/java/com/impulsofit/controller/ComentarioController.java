package com.impulsofit.controller;

import com.impulsofit.dto.request.ComentarioRequestDTO;
import com.impulsofit.dto.response.ComentarioResponseDTO;
import com.impulsofit.model.Comentario;
import com.impulsofit.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/publicacion/{publicacionId}/comentarios")
@PreAuthorize("hasRole('USER')")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @GetMapping
    public List<ComentarioResponseDTO> listarPorPublicacion(@PathVariable Long publicacionId) {
        return comentarioService.listarPorPublicacionDTO(publicacionId);
    }

    @GetMapping("/general")
    public List<ComentarioResponseDTO> listarGeneral(@PathVariable Long publicacionId) {
        return comentarioService.listarPorPublicacionGeneralDTO(publicacionId);
    }

    @GetMapping("/grupal")
    public List<ComentarioResponseDTO> listarGrupal(@PathVariable Long publicacionId) {
        return comentarioService.listarPorPublicacionGrupoDTO(publicacionId);
    }

    @PostMapping("/general")
    public ComentarioResponseDTO crearEnGeneral(@PathVariable Long publicacionId, @RequestBody ComentarioRequestDTO dto) {
        Comentario guardado = comentarioService.crearComentarioEnPublicacionGeneral(dto.usuarioId(), publicacionId, dto.contenido());
        return comentarioService.obtenerComentarioDTOPorId(guardado.getId());
    }

    @PostMapping("/grupal")
    public ComentarioResponseDTO crearEnGrupal(@PathVariable Long publicacionId, @RequestBody ComentarioRequestDTO dto) {
        Comentario guardado = comentarioService.crearComentarioEnPublicacionGrupo(dto.usuarioId(), publicacionId, dto.contenido());
        return comentarioService.obtenerComentarioDTOPorId(guardado.getId());
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long publicacionId, @PathVariable Long id) {
        comentarioService.eliminarComentario(id);
    }
}
