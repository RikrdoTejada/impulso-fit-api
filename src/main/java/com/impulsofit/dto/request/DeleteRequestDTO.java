package com.impulsofit.dto.request;

public record DeleteRequestDTO(
        String email,
        String constrasena,
        boolean confirm
) {
}
