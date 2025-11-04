package com.impulsofit.dto.response;

import java.util.List;

public record BusquedaResponseDTO (
    List<GrupoResponseDTO> grupos,
    List<UsuarioResponseDTO> usuarios,
    List<RetoResponseDTO> retos
){}

