package com.impulsofit.service;

import com.impulsofit.model.Comentario;
import com.impulsofit.repository.ComentarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    public List<Comentario> listarPorPublicacion(Long publicacionId) {
        return comentarioRepository.findByPublicacionId(publicacionId);
    }

    public Comentario crearComentario(Comentario comentario) {
        validarComentario(comentario);
        return comentarioRepository.save(comentario);
    }

    private void validarComentario(Comentario comentario) {
        String contenido = comentario.getContenido();

        if (!StringUtils.hasText(contenido)) {
            throw new IllegalArgumentException("El comentario no puede estar vacío (RN-12)");
        }

        if (contenido.length() > 300) {
            throw new IllegalArgumentException("El comentario no puede superar los 300 caracteres (RN-10)");
        }

        if (contenido.contains("http") || contenido.contains("www")) {
            throw new IllegalArgumentException("Los comentarios no pueden contener enlaces o archivos externos (RN-11)");
        }
    }

    public void eliminarComentario(Long id) {
        comentarioRepository.deleteById(id);
    }
}
