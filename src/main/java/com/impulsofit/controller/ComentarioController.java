package com.impulsofit.controller;

import com.impulsofit.dto.request.ComentarioRequestDTO;
import com.impulsofit.dto.response.ComentarioResponseDTO;
import com.impulsofit.model.Comentario;
import com.impulsofit.model.PublicacionGeneral;
import com.impulsofit.model.Usuario;
import com.impulsofit.service.ComentarioService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping("/publicacion/{id}")
    public List<ComentarioResponseDTO> listarPorPublicacion(@PathVariable Long id) {
        return comentarioService.listarPorPublicacion(id).stream()
                .map(c -> new ComentarioResponseDTO(
                        c.getId(),
                        c.getContenido(),
                        c.getUsuario().getNombre(),
                        c.getFechaCreacion()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ComentarioResponseDTO crearComentario(@RequestBody ComentarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getUsuarioId());

        PublicacionGeneral publicacion = new PublicacionGeneral();
        publicacion.setId(dto.getPublicacionId());

        Comentario comentario = new Comentario(dto.getContenido(), usuario, publicacion);
        Comentario guardado = comentarioService.crearComentario(comentario);

        return new ComentarioResponseDTO(
                guardado.getId(),
                guardado.getContenido(),
                guardado.getUsuario().getNombre(),
                guardado.getFechaCreacion()
        );
    }

    @DeleteMapping("/{id}")
    public void eliminarComentario(@PathVariable Long id) {
        comentarioService.eliminarComentario(id);
    }
}
