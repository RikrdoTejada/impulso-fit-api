package com.impulsofit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CrearRetoRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull Long createdById,
        @NotNull Long groupId
) {}