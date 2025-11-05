package com.impulsofit.dto.response;

import java.time.Instant;
import java.time.LocalDate;

public record RetoResponseDTO(
        Long id,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        Instant createdAt,
        Long createdById,
        Long groupId
) {}