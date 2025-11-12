package com.impulsofit.dto.response;

import java.util.List;

public record BusquedaResponseDTO (
    List<GrupoResponseDTO> grupos,
    List<PerfilResponseDTO> perfiles,
    List<RetoResponseDTO> retos
){}

