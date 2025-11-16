package com.impulsofit.dto.response;

import java.time.LocalDate;

public record MembresiaGrupoResponseDTO(
        Long id_membresia,
        String perfilNombre,
        String grupoNombre,
        LocalDate fecha_union
) {}