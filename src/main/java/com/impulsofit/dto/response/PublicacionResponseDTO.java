package com.impulsofit.dto.response;

import com.impulsofit.model.PublicacionType;

public record PublicacionResponseDTO(
        Long id_publicacion,
        String personaNombre,
        String nombrePerfil,
        PublicacionType tipo_publicacion,
        String grupoNombre,
        String contenido,
        String fecha_publicacion
) {
}