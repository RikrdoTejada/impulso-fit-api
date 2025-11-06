package com.impulsofit.dto.response;

import java.util.List;

public record FeedCompletoDTO(
    List<FeedResponseDTO> publicaciones,
    List<GrupoPopularDTO> gruposPopulares
)
{}