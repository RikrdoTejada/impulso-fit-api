package com.impulsofit.dto.request;

public record UsuarioRequestDTO(
        String username,
        String email,
        String password
) {}