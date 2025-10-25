package com.impulsofit.dto.response;

import java.time.LocalDate;

public record MembresiaGrupoResponse(
   Long id_membresia,
   String usuarioNombre,
   String grupoNombre,
   LocalDate fecha_union
) {}
