package com.impulsofit.dto.response;

import java.time.Instant;
import java.time.LocalDate;

public record RetoResponseDTO(
Long id,
String grupoNombre,
String creadorNombre,
String unidad,
String title,
String description,
Double objetivoTotal,
LocalDate fechaPublicacion,
LocalDate startDate,
LocalDate endDate,
Instant createdAt,
Long createdById,
Long groupId
) {}
