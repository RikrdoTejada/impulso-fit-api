package com.impulsofit.controller;

import com.impulsofit.model.Comentario;
import com.impulsofit.model.PublicacionGrupo;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.ComentarioRepository;
import com.impulsofit.repository.PublicacionGrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PublicacionGrupoRepository publicacionGrupoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crear un comentario en una publicación de grupo
    @PostMapping("/grupo/{publicacionId}/usuario/{usuarioId}")
    public Comentario crearComentario(@PathVariable Long publicacionId,
                                      @PathVariable Long usuarioId,
                                      @RequestBody String contenido) {

        PublicacionGrupo publicacion = publicacionGrupoRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comentario comentario = new Comentario(contenido, usuario, publicacion);
        return comentarioRepository.save(comentario);
    }

    // Obtener comentarios de una publicación de grupo
    @GetMapping("/grupo/{publicacionId}")
    public List<Comentario> obtenerComentarios(@PathVariable Long publicacionId) {
        PublicacionGrupo publicacion = publicacionGrupoRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        return comentarioRepository.findByPublicacionGrupoOrderByFechaCreacionAsc(publicacion);
    }
}
