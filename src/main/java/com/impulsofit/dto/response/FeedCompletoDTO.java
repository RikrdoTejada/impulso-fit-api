package com.impulsofit.dto.response;

import java.util.List;

public class FeedCompletoDTO {
    private List<FeedResponseDTO> publicaciones;
    private List<GrupoPopularDTO> gruposPopulares;

    // Constructores
    public FeedCompletoDTO() {}

    public FeedCompletoDTO(List<FeedResponseDTO> publicaciones, List<GrupoPopularDTO> gruposPopulares) {
        this.publicaciones = publicaciones;
        this.gruposPopulares = gruposPopulares;
    }

    // Getters y Setters
    public List<FeedResponseDTO> getPublicaciones() { return publicaciones; }
    public void setPublicaciones(List<FeedResponseDTO> publicaciones) { this.publicaciones = publicaciones; }

    public List<GrupoPopularDTO> getGruposPopulares() { return gruposPopulares; }
    public void setGruposPopulares(List<GrupoPopularDTO> gruposPopulares) { this.gruposPopulares = gruposPopulares; }
}