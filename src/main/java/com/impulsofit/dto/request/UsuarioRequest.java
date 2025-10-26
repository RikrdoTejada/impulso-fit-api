package com.impulsofit.dto.request;

public record UsuarioRequest(
        String nombre,
        String email,
        String contrasena,
        Integer edad,
        String genero
) {
}