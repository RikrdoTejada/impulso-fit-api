package com.impulsofit.dto.response;

import com.impulsofit.model.RoleType;

public record PerfilResponseDTO(
    Long idPerfil,
    String email,
    String nombres,
    RoleType role,
    Boolean bloqueado
) {}
