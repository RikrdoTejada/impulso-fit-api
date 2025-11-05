package com.impulsofit.dto.response;

import java.time.LocalDateTime;

public record ReaccionResponseDTO(
        Long id_reaccion,
        Long id_usuario,
        Long id_publicacion,
        LocalDateTime fecha_registro
        ) {
}
