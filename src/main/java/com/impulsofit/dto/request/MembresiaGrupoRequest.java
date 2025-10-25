package com.impulsofit.dto.request;

import java.time.LocalDate;

public record MembresiaGrupoRequest(
        Long id_usuario,
        Long id_grupo
) {
}
